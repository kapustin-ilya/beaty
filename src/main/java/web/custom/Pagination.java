package web.custom;

import lombok.Getter;
import lombok.Setter;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;
import java.io.IOException;

@SuppressWarnings("pagin")
public class Pagination extends TagSupport {
    private String command;

    @Override
    public int doStartTag() throws JspException {
        int numberPages = (Integer) pageContext.getSession().getAttribute("numberPages");
        int page = (Integer) pageContext.getSession().getAttribute("page");

        JspWriter out = pageContext.getOut();
        try {
            out.println("<div class=\"pagination\" style=\"padding-left: 40%;\">");
            if (page - 1 > 0){
                out.println(String.format("<a href=\"/beauty/b?command=%s&page=%d\"> Previous </a>",command,page-1));
            }
            for (int i = 1; i <= numberPages; i++){
                if (i == page){
                    out.println(String.format("<a class=\"active\" href=\"/beauty/b?command=%s&page=%d\"> %d </a>",command,i,i));
                } else {
                    out.println(String.format("<a href=\"/beauty/b?command=%s&page=%d\"> %d </a>",command,i,i));
                }
            }
            if (page + 1 <= numberPages){
                out.println(String.format("<a href=\"/beauty/b?command=%s&page=%d\"> Next </a>",command,page+1));
            }
            out.println("</div>");
        } catch (IOException e) {
            e.printStackTrace();
        }

        return SKIP_BODY;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

}
