package gdsc.skhu.ourth.domain.dto;

import lombok.Data;

@Data
public class ResponseDTO {

    private String status;

    private String message;

    private Object data;

}