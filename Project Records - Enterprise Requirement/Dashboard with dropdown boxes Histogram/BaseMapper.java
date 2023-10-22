@SqlResource("...")
public interface BaseMapper extends ...Mapper<BaseEmployeeEntity>{
	
    /**
	 * @author lu
	 * @date 2023/...
	 * @version 1.0
	 * ...
	*/     
    List<BaseEmployeeEntity> findLineOrProcess(BaseEmployeeEntity queryParam);
	
    /**
	 * @author lu
	 * @date 2023/...
	 * @version 1.0
	 * ...
	*/      
    PageResult<BaseVO> findDetail(PageRequest<BaseDTO> query, @Root Map pageParam);
   
    /**
	 * @author lu
	 * @date 2023/...
	 * @version 1.0
	 * ...
	*/      
    List<chartVO> plotChart(chartDTO query);
}