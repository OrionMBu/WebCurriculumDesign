package webcurriculumdesign.backend.data.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FileToUpload {
    private byte[] fileBytes;
    private String userMail;
    private String folderName;
    private String fileName;
    private boolean isBaseFile;
}
