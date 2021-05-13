package com.karangandhi.stackoverflow.clone.Components;

import com.google.firebase.auth.FirebaseAuthException;
import com.karangandhi.stackoverflow.clone.Services.FirebaseAuthService;
import com.karangandhi.stackoverflow.clone.Services.FirestoreService;

import java.util.ArrayList;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

public class Answer {
    public UUID id;
    public UUID userID;
    public UUID questionID;
    public String body;
    public String status;
    public ArrayList<Reputation> downVotes;
    public ArrayList<Comment> comments;
    public ArrayList<Reputation> upVotes;
    public ArrayList<Report> reports;
    public ArrayList<Edit> edits;

    public static final String STATUS_NORMAL = "normal";
    public static final String STATUS_ACCEPTED = "accepted";
    public static final String STATUS_COUNTERFEIT = "counterfeit";
    public static final String STATUS_NEEDS_TO_BE_FOCUSED = "needs to be focused";

    public Answer(String body, UUID userID, UUID questionID) throws ExecutionException, InterruptedException, FirebaseAuthException {
        this.id = UUID.randomUUID();
        this.userID = userID;
        this.body = body;
        this.status = STATUS_NORMAL + "; ";
        this.upVotes = new ArrayList<Reputation>();
        this.downVotes = new ArrayList<Reputation>();
        this.reports = new ArrayList<Report>();
        this.edits = new ArrayList<Edit>();
        this.comments = new ArrayList<Comment>();
        this.questionID = questionID;
        this.attachUser(this.getUser());
    }

    public Answer(UUID id, UUID userID, String body, String status, ArrayList<Reputation> upVotes, ArrayList<Reputation> downVotes, ArrayList<Report> reports, ArrayList<Edit> edits, ArrayList<Comment> comments, UUID questionID) {
        this.id = id;
        this.userID = userID;
        this.body = body;
        this.status = status;
        this.upVotes = upVotes;
        this.downVotes = downVotes;
        this.reports = reports;
        this.edits = edits;
        this.comments = comments;
        this.questionID = questionID;
    }

    public Question getQuestion() throws ExecutionException, InterruptedException {
        return Question.getQuestionById(this.questionID);
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
            this.status += (occurrences[0] > occurrences[1] ? STATUS_COUNTERFEIT : STATUS_NEEDS_TO_BE_FOCUSED) + "; ";
        }
        this.updateDatabase();
        return report;
    }

    public void accept() throws ExecutionException, InterruptedException, FirebaseAuthException {
        this.status += STATUS_ACCEPTED + "; ";
        this.getUser().addAnswerAccepted();
        this.updateDatabase();
    }

    public Edit addEdit(UUID uid, String body) throws ExecutionException, InterruptedException {
        int userReputation = this.getUser().getReputation();
        int _userReputation = FirebaseAuthService.getUser(uid).getReputation();
        Edit edit = new Edit(uid, body);
        // add the current state to the history
        this.edits.add(new Edit(this.id, this.body, Edit.STATUS_ACCEPTED));
        this.edits.add(edit);
        // if the current user has less reputation and the user making the edit has more reputation then add the edit without asking
        if (_userReputation > 20 && userReputation < 100) acceptEdit(edit);
        this.updateDatabase();
        return edit;
    }

    public Edit acceptEdit(Edit edit) throws ExecutionException, InterruptedException {
        for (Edit _edit : edits) {
            if (_edit.id.toString().equals(edit.toString())) {
                _edit.status = Edit.STATUS_ACCEPTED;
            }
        }
        this.body = edit.body;
        this.updateDatabase();
        return edit;
    }

    public Comment addComment(UUID uid, String body) throws ExecutionException, InterruptedException {
        Comment comment = new Comment(uid, body);
        this.comments.add(comment);
        this.updateDatabase();
        return comment;
    }

    public User getUser() throws ExecutionException, InterruptedException {
        return FirebaseAuthService.getUser(this.id);
    }

    public User attachUser(User user) throws FirebaseAuthException, ExecutionException, InterruptedException {
        user.addAnswer(this);
        return user;
    }

    public void addUpVote() throws ExecutionException, InterruptedException {
        this.upVotes.add(new Reputation(this.userID, Reputation.upVote));
        User user = getUser();
        this.updateDatabase();
    }

    public void addDownVote() throws ExecutionException, InterruptedException {
        this.upVotes.add(new Reputation(this.userID, Reputation.downVote));
        this.updateDatabase();
    }

    public void updateDatabase() throws ExecutionException, InterruptedException {
        FirestoreService.addData("answers", this.id.toString(), this);
    }

    public static Answer getAnswerById(UUID id) throws ExecutionException, InterruptedException {
        return FirestoreService.readData("answers", id.toString()).toObject(Answer.class);
    }

    @Override
    public String toString() {
        return "Answer{" +
                "id=" + id +
                ", userID=" + userID +
                ", body='" + body + '\'' +
                ", status='" + status + '\'' +
                ", downVotes=" + downVotes +
                ", comments=" + comments +
                ", upVotes=" + upVotes +
                ", reports=" + reports +
                ", edits=" + edits +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Answer answer = (Answer) o;
        return Objects.equals(id, answer.id) ||
                Objects.equals(body, answer.body);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userID, body, status, upVotes, downVotes, reports, edits, comments);
    }
}
