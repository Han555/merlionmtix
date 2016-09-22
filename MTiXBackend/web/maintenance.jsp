<%-- 
    Document   : maintenance
    Created on : Sep 20, 2016, 5:02:22 PM
    Author     : Student-ID
--%>

<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<jsp:useBean id="properties" class="java.util.List<entity.Property>" scope="request"/>
<jsp:include page="header.jsp" />
<!-- Main Content -->
<div class="side-body">
    <div class="page-title">
        <span class="title">Maintenance Schedule</span>
    </div>
    <div class="row">
        <div class="col-xs-12">
            <div class="card">
                <div class="card-header">
                    <div class="card-title">
                        <div class="title">Indicate Search Criteria</div>
                        <div class="description">Please take note Date Range is the whole period of time for the event (including setup and cleanup etc.)</div>
                    </div>
                </div>
                <div class="card-body">
                    <div style="padding-bottom: 20px;">
                        <table class="datatable table table-striped" cellspacing="0" width="100%">
                            <thead>
                                <tr>
                                    <th>No.</th>
                                    <th>Start Date</th>
                                    <th>End Date</th>
                                    <th>Delete</th>
                                </tr>
                            </thead>
                            <tbody id="items"></tbody>
                        </table>
                    </div>
                   <form class="form-horizontal" id="maintenanceForm" name="maintenanceForm"> 
                    <div class="form-group" style="padding-bottom: 50px;" >
                        <label for="propertyList" class="col-sm-2 control-label">Choose a Property</label>
                        <div class="col-sm-6">
                            <select class="js-example-basic-single js-states" id="propertyList" name="propertyList">
                                <c:forEach items="${properties}" var="properties">
                                    <option value="${properties.id}">${properties.propertyName}</option>
                                </c:forEach>	
                            </select>
                        </div>
                    </div>
                    
                    <div class="form-group" style="padding-bottom: 30px;" >
                        <label for="config-demo" class="col-sm-2 control-label">Choose a date range</label>
                        <div class="col-sm-6">
                            <input type="text" id="config-demo" class="form-control" name="mdaterange">
                            <i class="glyphicon glyphicon-calendar fa fa-calendar"></i>
                        </div>
                    </div>
                    <div class="form-group" style="padding-bottom: 60px;">
                        <div class="col-sm-offset-2 col-sm-10">
                            <button type="button" class="btn btn-default" value="msubmit" id="msubmit">Create New Maintenance Schedule</button>
                        </div>
                    </div>
                    </form> 
                </div>
            </div>
        </div>
    </div>


</div>

<div class="modal fade" id="confirm-peak" role="dialog">
    <div class="modal-dialog">

        <!-- Modal content-->
        <div class="modal-content">
            <div class="modal-body">
                <p>Your chosen period contains peak date(s). </p>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
            </div>
        </div>

    </div>
</div>

<script>
    var propertyId = 1;
    var blockedDates;
    var publicHolidays;

    function blockDates() {
        $.ajax({
            url: "propertyOccupiedDates?id=" + propertyId,
            success: function (result) {
                blockedDates = result;
            }
        });
    }

    function getMaintenance() {
        $.ajax({
            url: "maintenanceList?id=" + propertyId,
            success: function (result) {
                var str = "<tr>";
                for (var i = 0; i < result.length; i++) {
                    str += "<td>" + (i + 1) + "</td><td>" + result[i].startDate + "</td><td>" + result[i].endStart + "</td>";
                }
                $('#items').html(str);
            }
        });
    }

    $(document).ready(function () {

        blockDates();
        getMaintenance();
        $('#propertyList').change(function () {
            properyId = $(this).val();
            getMaintenance();
            blockDates();
        });

        $.ajax({
            url: "publicHolidays",
            success: function (result) {
                publicHolidays = result;
            }
        });

        $('#config-demo').daterangepicker(function (start, end, label) {
            console.log("New date range selected: ' + start.format('YYYY-MM-DD') + ' to ' + end.format('YYYY-MM-DD') + ' (predefined range: ' + label + ')");
        });

       $("#msubmit").bind("click", function(){
        $.ajax({
            type: "POST",
            url: 'CreateNewMaintenance',
            data: $("#maintenanceForm").serialize(),
            success: function (result) {
                if(result[0]="success"){
                    alert("success");
                }
                getMaintenance();
            }
        });
        console.log($("#maintenanceForm").serialize());
        
    });
    });




</script>
<jsp:include page="footer.jsp" />


