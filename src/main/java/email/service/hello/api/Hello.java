package email.service.hello.api;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class Hello {

  @JsonProperty
  private Long id;

  @JsonProperty
  @NotNull
  @Size(min=5, max=1)
  private String saying;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getSaying() {
    return saying;
  }

  public void setSaying(String saying) {
    this.saying = saying;
  }
}
