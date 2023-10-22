/**
 * @ClassName PersonnelTraceabilityDTO
 * @Description TODO
 * @Author lhj
 * @DATE 2023/10/22 1:56
 * @Version 1.0
 */
@Data
public class PersonnelTraceabilityDTO extends PageParam{

    private String projectCode;
    private String lineCode;
    private String employeeId;
    private String name;
    private String beginDate;
    private String endDate;

}
