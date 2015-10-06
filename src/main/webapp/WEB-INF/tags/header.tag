<%@tag description="Simple Wrapper Tag" pageEncoding="UTF-8"%>
<nav class="navbar navbar-default" role="navigation">
    <div class="container-fluid">
        <!-- Brand and toggle get grouped for better mobile display -->
        <div class="navbar-header">
            <button type="button" class="navbar-toggle" data-toggle="collapse" data-target="#bs-example-navbar-collapse-1">
                <span class="sr-only">Toggle navigation</span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
            </button>
        </div>
        
        <!-- Collect the nav links, forms, and other content for toggling -->
        <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
            <ul class="nav navbar-nav">
                <li class=""><a href="${pageContext.request.contextPath}/home">หน้าแรก</a></li>
                <li class=""><a href="${pageContext.request.contextPath}/addexternal">เพิ่มหนังสือส่ง - ออกภายนอก</a></li>
                <li class=""><a href="${pageContext.request.contextPath}/addinternal">เพิ่มหนังสือรับ - จากภายนอก</a></li>
            </ul>
        </div><!-- /.navbar-collapse -->
    </div><!-- /.container-fluid -->
</nav>
