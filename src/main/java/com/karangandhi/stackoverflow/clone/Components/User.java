package com.karangandhi.stackoverflow.clone.Components;

import com.google.cloud.firestore.WriteResult;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;
import com.google.firebase.database.utilities.Pair;
import com.karangandhi.stackoverflow.clone.Services.FirebaseAuthService;

import java.util.ArrayList;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

public class User {
    public UUID id;
    public String displayName, profilePic, username, password, email;
    public ArrayList<Reputation> reputation;
    public ArrayList<UUID> questions;
    public ArrayList<UUID> answers;

    public User(String displayName, String profilePic, String username, String password, String email) {
        this.displayName = displayName;
        this.profilePic = profilePic;
        this.username = username;
        this.password = password;
        this.reputation = new ArrayList<>();
        this.questions = new ArrayList<>();
        this.answers = new ArrayList<>();
        this.email = email;
    }

    public User(UUID id, String displayName, String profilePic, String username, String password, ArrayList<Reputation> reputation, ArrayList<UUID> questions, ArrayList<UUID> answers, String email) {
        this.id = id;
        this.displayName = displayName;
        this.profilePic = profilePic;
        this.username = username;
        this.password = password;
        this.reputation = reputation;
        this.questions = questions;
        this.answers = answers;
        this.email = email;
    }

    public void setDisplayName(String displayName) throws FirebaseAuthException, ExecutionException, InterruptedException {
        this.displayName = displayName;
        this.updateUserDatabase();
    }

    public void setPassword(String password) throws FirebaseAuthException, ExecutionException, InterruptedException {
        this.password = password;
        this.updateUserDatabase();
    }

    public void setEmail(String email) throws FirebaseAuthException, ExecutionException, InterruptedException {
        this.email = email;
        this.updateUserDatabase();
    }

    public void setUsername(String username) throws FirebaseAuthException, ExecutionException, InterruptedException {
        this.username = username;
        this.updateUserDatabase();
    }

    public void setProfilePic(String profilePic) throws FirebaseAuthException, ExecutionException, InterruptedException {
        this.profilePic = profilePic;
        this.updateUserDatabase();
    }

    public void addQuestion(Question question) throws FirebaseAuthException, ExecutionException, InterruptedException {
        this.questions.add(question.id);
        this.updateUserDatabase();
    }

    public void addAnswer(Answer answer) throws FirebaseAuthException, ExecutionException, InterruptedException {
        this.answers.add(answer.id);
        this.updateUserDatabase();
    }

    public void addUpvote() throws FirebaseAuthException, ExecutionException, InterruptedException {
        this.reputation.add(new Reputation(this.id, Reputation.upVote));
        this.updateUserDatabase();
    }

    public void addDownVote() throws FirebaseAuthException, ExecutionException, InterruptedException {
        this.reputation.add(new Reputation(this.id, Reputation.downVote));
        this.updateUserDatabase();
    }

    public void addAnswerAccepted() throws FirebaseAuthException, ExecutionException, InterruptedException {
        this.reputation.add(new Reputation(this.id, Reputation.answerAccepted));
        this.updateUserDatabase();
    }

    public void addReputation(Reputation rep) throws FirebaseAuthException, ExecutionException, InterruptedException {
        this.reputation.add(rep);
        this.updateUserDatabase();
    }

    public Pair<UserRecord, WriteResult> registerUser() throws FirebaseAuthException, ExecutionException, InterruptedException {
        return FirebaseAuthService.createUser(this);
    }

    public Pair<UserRecord, WriteResult> updateUserDatabase() throws FirebaseAuthException, ExecutionException, InterruptedException {
        return FirebaseAuthService.updateUser(this);
    }

    public void deleteUser() throws FirebaseAuthException, ExecutionException, InterruptedException {
        FirebaseAuthService.deleteUser(this);
    }

    public int getReputation() {
        int rep = 0;
        for (Reputation _reputation : this.reputation) rep += _reputation.type.equals(Reputation.upVote) ? 
                Reputation.upVoteValue : _reputation.type.equals(Reputation.answerAccepted) ?
                Reputation.answerAcceptedValue : _reputation.type.equals(Reputation.gotCorrectAnswer) ?
                Reputation.gotCorrectAnswerValue : Reputation.downVoteValue;
        return rep;
    }

    public static Pair<UserRecord, WriteResult> UploadUserToDatabase(User user) throws FirebaseAuthException, ExecutionException, InterruptedException {
        return FirebaseAuthService.createUser(user);
    }

    public static User getUserFromDatabase(UUID id) throws ExecutionException, InterruptedException {
        return FirebaseAuthService.getUser(id);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", displayName='" + displayName + '\'' +
                ", profilePic='" + profilePic + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", reputation=" + reputation +
                ", questions=" + questions +
                ", answers=" + answers +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id) ||
                Objects.equals(username, user.username) ||
                Objects.equals(email, user.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, displayName, profilePic, username, password, email, reputation, questions, answers);
    }
}
