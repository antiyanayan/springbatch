<#import "/spring.ftl" as spring />
<#if jobExecutionInfo??>
<#assign url><@spring.url relativeUrl="${servletPath}/jobs/executions/${jobExecutionInfo.id?c}.json"/></#assign>
"jobExecution" : {
"resource" : "${baseUrl}${url}",
"id" : "${jobExecutionInfo.id?c}",
"name" : "${jobExecutionInfo.name}",
"status" : "${jobExecutionInfo.jobExecution.status}",
"startTime" : "${jobExecutionInfo.startTime}",
"duration" : "${jobExecutionInfo.duration}",
"exitCode" : "${jobExecutionInfo.jobExecution.exitStatus.exitCo de}",
"exitDescription" : "${jobExecutionInfo.jobExecution.exitStatus.exitDe scription?replace('\t','\\t')?replace('\n','\\n')? replace('\r','')}",
<#assign url><@spring.url relativeUrl="${servletPath}/jobs/${jobExecutionInfo.name}/${jobExecutionInfo.jobId?c}.json"/></#assign>
"jobInstance" : { "resource" : "${baseUrl}${url}" },
}
</#if>