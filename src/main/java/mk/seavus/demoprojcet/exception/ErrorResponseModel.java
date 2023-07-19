package mk.seavus.demoprojcet.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponseModel {

  private int status;
  private String error;
  private String message;
  private String path;
  private String date;
  
}
