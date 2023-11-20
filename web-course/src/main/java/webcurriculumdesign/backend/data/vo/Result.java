package webcurriculumdesign.backend.data.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Result {
    private Integer code;
    private Object data;
    private String msg;

    public static Result success(Object data){
        return new Result(200, data, null);
    }
    public static Result ok(){
        return new Result(200, null, null);
    }
    public static Result error(Integer code, String msg){
        return new Result(code, null, msg);
    }}
