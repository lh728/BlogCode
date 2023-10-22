/**
 * @ClassName PersonnelTraceabilityService
 * @Description TODO
 * @Author lhj
 * @DATE 2023/10/22 1:54
 * @Version 1.0
 */
@Service
public interface PersonnelTraceabilityService extends SysBaseService<personnelTraceabilityMapper, BaseCallNamesEntity>{
    public PageResult<Map<String, Object>> findDeatil(PersonnelTraceabilityDTO queryParam);
    public void exportExcel(PersonnelTraceabilityDTO queryParams, HttpServletResponse response) throws IOException;
}