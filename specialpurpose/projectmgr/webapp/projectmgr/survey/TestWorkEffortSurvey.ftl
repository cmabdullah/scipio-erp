<#--
Licensed to the Apache Software Foundation (ASF) under one
or more contributor license agreements.  See the NOTICE file
distributed with this work for additional information
regarding copyright ownership.  The ASF licenses this file
to you under the Apache License, Version 2.0 (the
"License"); you may not use this file except in compliance
with the License.  You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing,
software distributed under the License is distributed on an
"AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
KIND, either express or implied.  See the License for the
specific language governing permissions and limitations
under the License.
-->

<@section title="${survey.surveyName}">
        <#-- Render the survey -->
        <#if surveyWrapper?has_content>
            <form method="post" enctype="multipart/form-data" action="<@ofbizUrl>testWorkEffortSurveyResponse</@ofbizUrl>">
              <input type="hidden" name="productStoreSurveyId" value="${productStoreSurveyId}"/>
              <input type="hidden" name="workEffortId" value="${workEffortId}"/>
              ${surveyWrapper.render(context)}
            </form>
        <#else>
            <@resultMsg>${uiLabelMap.OrderNothingToDoHere}</@resultMsg>
        </#if>
</@section>
