package sirs.webinterface.presentation;


import common.sirs.ws.NoteView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import sirs.webinterface.domain.NotesManager;

import java.util.List;

@Controller
@RequestMapping(value = "/dashboard/{username}")
public class DashboardController {
    private static Logger logger = LoggerFactory.getLogger(DashboardController.class);

    @RequestMapping(method = RequestMethod.GET)
    public String getNoteListing(Model model, @PathVariable String username) {
        List<NoteView> notes = NotesManager.getInstance().askForAllUserNotes(username);

        model.addAttribute("notes", notes); // notes the user has access to
        model.addAttribute("note", new NoteView()); // to fill in

        return "dashboard";
    }

    @RequestMapping(method = RequestMethod.POST)
    public String updateNoteSubmit(NoteView noteView, @PathVariable String username) {
        noteView.setOwner(username);
        NotesManager.getInstance().updateNote(noteView);
        return "redirect:/dashboard/"+username;
    }
}
