package blog.helpers;


//CUTOM REPONSE OF API

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ApiResponse {
	public ApiResponse(String meassage, boolean success) {
		super();
		this.meassage = meassage;
		this.success = success;
	}
	private String meassage;
	private boolean success;
	

}
