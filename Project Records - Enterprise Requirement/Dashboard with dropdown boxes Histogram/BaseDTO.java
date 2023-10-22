    /** 
	 * @author lu
	 * @date 2023/...
	 * @version 1.0
	 * ...
	*/   

@Data
@ApiModel("BaseDTO")
public class BaseDTO extends PageParam implements Serializable{
    
    private static final long serialVersionUID = ...;
    
    @Query
    @ApiModelProperty("deptCode")
    private String deptCode;
    
    @Query
    @ApiModelProperty("process")
    private String processCode;
    @Query
    @ApiModelProperty("line")
    private String lineCode;
    
    @Query
    @ApiModelProperty("type")
    private String type;
    
    @Query
    @ApiModelProperty("beginDate")
    private String beginDate;
    
    @Query
    @ApiModelProperty("endDate")
    private String endDate;
    
    @Query
    @ApiModelProperty("name")
    private String name;
    
    @Query
    @ApiModelProperty("employeeId")
    private String employeeId;
    
}