<%-- 
    Document   : ConcertHallSelected
    Created on : Sep 20, 2016, 10:50:03 AM
    Author     : catherinexiong
--%>

<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<jsp:include page="header.jsp" />
<!-- Main Content -->

<div class="side-body">
    <div class="container">
        <div class="page-title">
            <span class="title" style="font-size: 40px;color: #22A7F0">Merlion Concert Hall</span>
        </div>
        <div class="row">
            <div class="col-sm-8">

                <img src="img/property/Concerthall_layout.png" alt="concert_main" style="width:100%;"> </div>
            <div class="col-sm-4">
                <div class="card">
                    <div class="card-header">
                        <div class="card-title">
                            <div class="title">Additional Requirement</div>
                            <div class="description">Please indicate additional needs for equipment and manpower (with extra charges)</div>
                        </div>
                    </div>
                    <div class="card-body">
                        <form class="form-horizontal" id="formSubmit" action="${formAction}" method="post">
                            <div class="form-group" style="padding-bottom: 20px;" >
                                <label for="equipment" class="col-sm-4 control-label">Equipment</label>
                                <div class="col-sm-8">
                                    <select class="js-example-basic-multiple js-states form-control" multiple="multiple" style="width: 100%" name="equipment" id="equipment">
                                        <option value="SS">Small Scale</option>
                                        <option value="SS">Medium Scale</option>
                                        <option value="LS">Large Scale</option>

                                    </select>
                                </div> 
                            </div>


                            <div class="form-group" style="padding-bottom: 20px;">
                                
                                    <label for="manpower" class="col-sm-4 control-label">Manpower</label>
                                    <div class="col-sm-8">
                                    <select class="js-example-basic-multiple js-states form-control" multiple="multiple" style="width: 100%;" name="manpower" id="manpower">
                                        <option value="SS">Small Scale</option>
                                        <option value="SS">Medium Scale</option>
                                        <option value="LS">Large Scale</option>
                                    </select>
                                </div>
                            </div>
                            <div class="form-group" style="padding-bottom: 20px;">
                                <div class="col-sm-10">
                                    <button type="submit" class="btn btn-default" value="formSubmit">Reserve Venue</button>
                                </div>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<script>
    var id = 0;
    $(".p1").click(function () {
        id = $(this).attr('id');

        $(".modal-header #myModalLabel").text('Detailed Seats Arrangement #' + id);
        $("#popup").html("LOADING...");
        $("#popup").html('<iframe id="p1frame" class="embed-responsive-item" frameborder="0" src="http://localhost:8080/MTiX-war/seat.jsp?id=' + id + '"></iframe>');

        //console.log('"http://localhost:8080/MTiX-war/seat.jsp?id=' + id + '"');
    });
</script>
<div class="modal fade modal-primary" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                <h4 class="modal-title" id="myModalLabel">Detailed seats</h4>
            </div>
            <div class="modal-body">
                <div class="embed-responsive embed-responsive-16by9" id="popup">
                    <!---
                    <iframe id="p1frame" class="embed-responsive-item" frameborder="0" src="http://localhost:8080/MTiX-war/seat.jsp?id="></iframe>
                    -->
                </div>


                <div class="modal-footer">
                    <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                    <button type="button" class="btn btn-primary">OK</button>
                </div>
            </div>
        </div>
    </div>


    <jsp:include page="footer.jsp" />
