/**
 * @ClassName personnelTraceabilityServiceImpl
 * @Description TODO
 * @Author lhj
 * @DATE 2023/10/22 2:02
 * @Version 1.0
 */
@Service
public class personnelTraceabilityServiceImpl extends SysBaseService<personnelTraceabilityMapper, BaseCallNamesEntit>
        implements PersonnelTraceabilityService{

    @Override
    public PageResult<Map<String, Object>> findDeatil(PersonnelTraceabilityDTO queryParam) {
        LocalDate startDate = LocalDate.parse(queryParam.getBeginDate(), DateTimeFormatter.ISO_DATE);
        LocalDate endDate = LocalDate.parse(queryParam.getEndDate(), DateTimeFormatter.ISO_DATE);

        List<String> dateList = getDateList(startDate,endDate);

        String sql = getSql(queryParam,dateList);
        PageRequest request = DefaultPageRequest.of(queryParam.getPage(),queryParam.getLimit);
        PageResult<Map<String, Object>> result = getSqlManager().execute(new SQLReady(sql), Map.class, request);
        return result;
    }

    private String getSql(PersonnelTraceabilityDTO queryParam, List<String> dateList) {
        String sql = SqlUtils.formatSql("select * from (select distinct " +
                "project_Name as projectName" +
                "Line_Name as lineName," +
                "job_Name as jobName," +
                "EmployeeId || '/' || EmployeeName as employeeInfo," +
                "D_date," +
                "from NEW_BASE_CALL_NAMES" +
                "where employeeId is not null" +
                (StringUtils.isNotEmpty(queryParam.getProjectCode()) ? " and project_code = '" + queryParam.getProjectCode() + "'" : "") +
                (StringUtils.isNotEmpty(queryParam.getLineCode()) ? " and line_code = '" + queryParam.getLineCode() + "'" : "") +
                (StringUtils.isNotEmpty(queryParam.getEmployeeId()) ? " and employeeId like '%" + queryParam.getProjectCode() + "%'" : "") +
                (StringUtils.isNotEmpty(queryParam.getName()) ? " and name like '%" + queryParam.getName() + "%'" : "") +
                "and d_date >= '" + queryParam.getBeginDate() + "' and d_date <= '" + queryParam.getEndDate() +
            "' ) pivot (listagg(employeeInfo, ',') with group (order by employeeInfo) for d_date in " +
            Tools.listToStr(dateList) + ")");
        return sql;
    }

    private List<String> getDateList(LocalDate startDate, LocalDate endDate) {
        ArrayList<String> dateList = new ArrayList<>();
        LocalDate currentDate = startDate;
        while (!currentDate.isAfter(endDate)){
            dateList.add(currentDate.format(DateTimeFormatter.ISO_DATE));
            currentDate = currentDate.plusDays(1);
        }
        return dateList;
    }

    @Override
    public void exportExcel(PersonnelTraceabilityDTO queryParams, HttpServletResponse response) throws IOException {
        List<Map> list = findList(queryParams);

        // TranslatedMap
        Map<String,String> translatedMap = new HashMap<>();
        translatedMap.put(...);

        List<String> headers = getTranslatedHeaders(list,translatedMap);

        List<List<Object>> datalist = getTranslatedData(list, translatedMap, headers);

        File file = new File( "");
        String filePath = null;
        filePath = file.getCanonicalPath();
        filePath = filePath +"/downloadExportTemplate";
        if (!FileUtil.isDirectory(filePath)){
            FileUtil.mkdir(filePath);
        }
        String fileName = filePath.concat( "/personnelTraceability.xls");

        ExcelWriterBuilder writerBuilder = EasyExcel.write(fileName)
                // set column width
                .registerWriteHandler(new SimpleColumnWidthStyleStrategy(25))
                .head(createdHead(headers));
        writerBuilder.sheet("personnelTraceability").doWrite(datalist);

        InputStream in = new FileInputStream(fileName);
        String encode = URLEncoder.encode(fileName, "UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/vnd.ms-excel;charset=utf-8");
        response.setHeader("Content-Disposition","attachment; filename=" + encode);
        HSSFWorkbook workbook = new HSSFWorkbook(in);
        workbook.write(response.getOutputStream());
        in.close();
    }


    private List<List<String>> createdHead(List<String> headerMap) {
        List<List<String>> headList = new ArrayList<>();
        // change headers to required List<List<Object>>
        for (String head : headerMap) {
            ArrayList<String> list = new ArrayList<>();
            list.add(head);
            headList.add(list);
        }
        return headList;
    }

    private List<List<Object>> getTranslatedData(List<Map> list, Map<String, String> translatedMap, List<String> headers) {
        // translated and replace single quotes
        for (Map<String, Object> map : list){
            HashMap<String, Object> newMap = new HashMap<>();
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                String key = entry.getKey();
                Object value = entry.getValue();
                if (translatedMap.containsKey(key)){
                    String newKey = translatedMap.get(key);
                    newMap.put(newKey,value);
                }else {
                    // Eliminate single quotes on both sides of a string 'yyyy-mm-dd'
                    String replace = key.replace("'", "");
                    newMap.put(replace,value);
                }
            }
            map.clear();
            map.putAll(newMap);
        }
        // change data to required List<List<Object>>
        List<List<Object>> lists = new ArrayList<>();
        for (Map<String,Object> map : list) {
            ArrayList<Object> objects = new ArrayList<>();
            for (int i = 0; i < headers.size(); i++) {
                objects.add(map.get(headers.get(i)));
            }
            lists.add(objects);
        }
        return lists;
    }

    private List<String> getTranslatedHeaders(List<Map> list, Map<String, String> translatedMap) {
        // just need first row data's keys
        LinkedHashMap<String,Object> firstRow = (LinkedHashMap) list.get(0);
        ArrayList<String> headers = new ArrayList<>();
        for (Map.Entry<String, Object> entry : firstRow.entrySet()) {
            headers.add(entry.getKey());
        }
        for (int i = 0; i < headers.size(); i++) {
            String key = headers.get(i);
            if(translatedMap.containsKey(key)){
                String s = translatedMap.get(key);
                headers.set(i,s);
            }else{
                // Eliminate single quotes on both sides of a string 'yyyy-mm-dd'
                String replace = key.replace("'", "");
                headers.set(i,replace);
            }
        }
        return headers;
    }

    private List<Map> findList(PersonnelTraceabilityDTO queryParams) {
        LocalDate startDate = LocalDate.parse(queryParams.getBeginDate(), DateTimeFormatter.ISO_DATE);
        LocalDate endDate = LocalDate.parse(queryParams.getEndDate(), DateTimeFormatter.ISO_DATE);

        List<String> dateList = getDateList(startDate,endDate);

        String sql = getSql(queryParams,dateList);
        List<Map> result = getSqlManager().execute(new SQLReady(sql),Map.class);
        return result;
    }
}
