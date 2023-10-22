findDetail
====

SELECT -- @pageTag(){
name,employeeId,sex...
-- @}
FROM(
nbe.name as name,
nbe.employeeId as employeeId,
...
FROM
employee nbe INNER JOIN(
SELECT nbe.deptId,nbd.deptName
    FROM department nbd START WITH nbd.deptId = #{deptCode} 
    CONNECT BY nbd.up_deptId = PRIOR nbd.deptId
) nbd ON nbd.deptId = nbe.deptId
LEFT JOIN ...
WHERE 1=1
-- @if(!isEmpty(projectCode) || !isBlank(projectCode)){
	and nbe.projectCode = #{projectCode}
-- @}
-- @if(...){
	...
-- @}
...
GROUP BY
nbe.name,
nbe.employeeId,
...)

findLineOrProcess
====
SELECT line_type,line_code,line_name
from A
where project_code = #{projectCode}
group by line_type,line_code,line_name
union
select '3',line_code,line_name
from B
where project_code = #{projectCode}
group by line_code,line_name