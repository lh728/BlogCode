    /** 
	 * @author lu
	 * @date 2023/...
	 * @version 1.0
	 * ...
	*/   
@Table(name = "...")
@Data
@TargetSQLManager("sqlManager")
public class BaseEmployeeEntity{
    
    @AssignId
    @Column("ID")
    private String id;
    
    @Column("employeeId")
    @Excel(name="employeeId")
    private String employeeId;
    
    ...
    
}