<html>
<body>
<pre>
HELLO VM
    #{121.2322;m2M2}
    <#--
    here cannot be seen
    -->
    ${value1}
   <#--
   output value1
   -->
<#list colors as n>
    color:${n}

</#list>
<#list map?keys as key>
key:${key}
values:${map[key]}
</#list>
<#---${user.getUsername()}
    ${user.name}-->
<#assign title='111111'>
<#include "header.ftl" parse=true/>
<#function getColor arg1 arg2>
<#return "Color by getColor  "+arg1+","+arg2>
</#function>
<#list colors as n>
${getColor(n,n_index)}
</#list>
<#assign hello1="hello">
<#assign  hello2="${hello1} world">
<#assign  hello3='${hello1} world'>
    hello2:${hello2}
    hello3:${hello3}
    size;${colors.size()}
</pre>
</body>
</html>