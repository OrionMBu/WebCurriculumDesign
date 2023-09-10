package webcurriculumdesign.backend.data.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MailTemplate {
    private String to;
    private String subject;
    private String content;
    private boolean isHtml;
}
