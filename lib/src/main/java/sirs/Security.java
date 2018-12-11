package sirs;

import common.sirs.ws.NoteDigestView;
import common.sirs.ws.NoteView;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Security {

    public static byte[] buildDigestFrom(String input){
        return buildDigestFrom(input.getBytes(StandardCharsets.UTF_8));
    }

    public static byte[] buildDigestFrom(byte[] input){
        try{
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            return digest.digest(input);
        } catch(NoSuchAlgorithmException e){
            e.printStackTrace();
        }
        return null;
    }

    public static NoteDigestView buildNoteDigestView(NoteView noteView){
        NoteDigestView noteDigestView = new NoteDigestView();

        noteDigestView.setNoteView(noteView);
        noteDigestView.setDigest(buildDigestFrom(noteView.getContent()));

        return noteDigestView;
    }
}
