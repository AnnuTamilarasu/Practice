import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("bgImgPath", "/images/Sudoku.png"); // put in static/images/
        return "home"; // home.html template
    }
}
