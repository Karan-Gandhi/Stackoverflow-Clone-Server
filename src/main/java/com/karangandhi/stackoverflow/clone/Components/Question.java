package com.karangandhi.stackoverflow.clone.Components;

import com.google.cloud.firestore.WriteResult;
import com.google.firebase.auth.FirebaseAuthException;
import com.karangandhi.stackoverflow.clone.Services.FirebaseAuthService;
import com.karangandhi.stackoverflow.clone.Services.FirestoreService;

import java.util.ArrayList;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

public class Question {
    public UUID id;
    public UUID userID;
    public String title;
    public String body;
    public String status; // keep a history of all the status split it by ; to get a array
    public ArrayList<Edit> edits;
    public ArrayList<Reputation> upVotes;
    public ArrayList<Reputation> downVotes;
    public ArrayList<Report> reports;
    public ArrayList<UUID> answers;
    public ArrayList<Comment> comments;
    public Answer acceptedAnswer = null;

    public static final String STATUS_OPEN = "open; ";
    public static final String STATUS_ANSWERED = "answered; ";
    public static final String STATUS_CLOSED = "closed; ";
    public static final String STATUS_DUPLICATE = "duplicate; ";
    public static final String STATUS_NEEDS_TO_BE_FOCUSED = "needs to be focused; ";

    public Question(String title, String body, UUID userID) {
        this.title = title;
        this.body = body;
        this.userID = userID;
        this.id = UUID.randomUUID();
        this.edits = new ArrayList<Edit>();
        this.upVotes = new ArrayList<Reputation>();
        this.downVotes = new ArrayList<Reputation>();
        this.reports = new ArrayList<>();
        this.answers = new ArrayList<>();
        this.comments = new ArrayList<>();
        this.status = STATUS_OPEN;
    }

    public Question(UUID id, UUID userID, String title, String body, String status, ArrayList<Edit> edits, ArrayList<Reputation> upVotes, ArrayList<Reputation> downVotes, ArrayList<Report> reports, ArrayList<UUID> answers, ArrayList<Comment> comments, Answer acceptedAnswer) {
        this.id = id;
        this.userID = userID;
        this.title = title;
        this.body = body;
        this.status = status;
        this.edits = edits;
        this.upVotes = upVotes;
        this.downVotes = downVotes;
        this.reports = reports;
        this.answers = answers;
        this.comments = comments;
        this.acceptedAnswer = acceptedAnswer;
    }

    public Reputation addUpVote() throws FirebaseAuthException, ExecutionException, InterruptedException {
        Reputation rep = new Reputation(this.userID, Reputation.upVote);
        this.upVotes.add(rep);
        this.getUser().addUpvote();
        this.updateDatabase();
        return rep;
    }

    public Reputation addDownVote() throws FirebaseAuthException, ExecutionException, InterruptedException {
        Reputation rep = new Reputation(this.userID, Reputation.downVote);
        this.downVotes.add(rep);
        this.getUser().addDownVote();
        this.updateDatabase();
        return rep;
    }

    public Reputation acceptAnswer(UUID id) throws ExecutionException, InterruptedException, FirebaseAuthException {
        Answer answer = Answer.getAnswerById(id);
        User currentUser = this.getUser();
        answer.accept(); // this will also update the reputation of the person who asked answered the question
        Reputation rep = new Reputation(this.userID, Reputation.gotCorrectAnswer);
        currentUser.addReputation(rep); // add the reputation to the user who asked the question
        this.acceptedAnswer = answer;
        this.status += STATUS_ANSWERED;
        this.updateDatabase();
        return rep;
    }

    public Report addReport(String type) throws ExecutionException, InterruptedException {
        Report report = new Report(this.id, type);
        this.reports.add(report);
        if (reports.size() > 3) {
            int[] occurrences = new int[2];
            for (Report _report : reports) {
                if (_report.type.equals(Report.TYPE_DUPLICATE)) occurrences[0]++;
                else if (_report.type.equals(Report.TYPE_NEEDS_TO_BE_FOCUSED)) occurrences[1]++;
            }
            this.status += occurrences[0] > occurrences[1] ? STATUS_DUPLICATE : STATUS_NEEDS_TO_BE_FOCUSED;
            if (occurrences[0] + occurrences[1] > 5) this.status += STATUS_CLOSED;
        }
        this.updateDatabase();
        return report;
    }

    public Comment addComment(UUID uid, String body) throws ExecutionException, InterruptedException {
        Comment comment = new Comment(uid, body);
        this.comments.add(comment);
        this.updateDatabase();
        return comment;
    }
    
    public void addAnswer(Answer answer) throws ExecutionException, InterruptedException {
        this.answers.add(answer.id);
        this.updateDatabase();
    }

    public Question attachUser() throws FirebaseAuthException, ExecutionException, InterruptedException {
        this.getUser().addQuestion(this);
        return this;
    }

    public User getUser() throws ExecutionException, InterruptedException {
        return FirebaseAuthService.getUser(this.userID);
    }

    public WriteResult updateDatabase() throws ExecutionException, InterruptedException {
        return FirestoreService.addData("questions", this.id.toString(), this);
    }
    
    public static Question getQuestionById(UUID id) throws ExecutionException, InterruptedException {
        return FirestoreService.readData("questions", id.toString()).toObject(Question.class);
    }

    @Override
    public String toString() {
        return "Question{" +
                "id=" + id +
                ", userID=" + userID +
                ", title='" + title + '\'' +
                ", body='" + body + '\'' +
                ", status='" + status + '\'' +
                ", edits=" + edits +
                ", upVotes=" + upVotes +
                ", downVotes=" + downVotes +
                ", reports=" + reports +
                ", answers=" + answers +
                ", comments=" + comments +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Question question = (Question) o;
        return Objects.equals(id, question.id) ||
                Objects.equals(title, question.title) ||
                Objects.equals(body, question.body);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userID, title, body, status, edits, upVotes, downVotes, reports, answers, comments);
    }
}
