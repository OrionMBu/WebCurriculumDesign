package webcurriculumdesign.backend.data.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum TokenType {

    ACCESS("ACCESS"),
    REFRESH("REFRESH");

    public final String type;
}
