<!DOCTYPE html>
<html id="ng-app" ng-app="app"> <!-- id="ng-app" IE<8 -->

    <head>
        <title>Simple example</title>
        <link rel="stylesheet" href="css/bootstrap.min.css" />

        <!-- Fix for old browsers -->
        <script src="js/jquery-1.8.3.min.js"></script>
        <!--  <script src="../console-sham.js"></script>
-->
        <script src="js/bootstrap.min.js"></script>

        <!--<script src="../bower_components/angular/angular.js"></script>-->
        <script src="js/angular.min.js"></script>
        <script src="js/angular-file-upload.min.js"></script>
        <script src="js/controllers.js"></script>

        <style>
            .my-drop-zone { border: dotted 3px lightgray; }
            .nv-file-over { border: dotted 3px red; } /* Default class applied to drop zones on over */
            .another-file-over-class { border: dotted 3px green; }

            html, body { height: 100%; }
        </style>

    </head>

    <!-- 1. nv-file-drop="" uploader="{Object}" options="{Object}" filters="{String}" -->
    <body ng-controller="AppController" nv-file-drop="" uploader="uploader" filters="queueLimit, customFilter">

        <div class="container">

            <div class="navbar navbar-default">
                <div class="navbar-header">
                    <a class="navbar-brand" >Pathloss File Upload</a>
                </div>
                
            </div>

            <div class="row">

                <div class="col-md-3">

                    <h3>Select files</h3>

                    <!-- Example: nv-file-select="" uploader="{Object}" options="{Object}" filters="{String}" -->
                    Multiple
                    <input type="file" nv-file-select="" uploader="uploader" multiple  /><br/>

<!--                     Single -->
<!--                     <input type="file" nv-file-select="" uploader="uploader" /> -->
					<div>
                     <button type="button" class="btn btn-success btn-s" ng-click="startService()" >
                            <span class="glyphicon glyphicon-upload"></span> Start Service
                        </button>
                    </div>
                </div>

                <div class="col-md-9" style="margin-bottom: 40px">

                    <h3>Upload queue</h3>
                    <p>Queue length: {{ uploader.queue.length }}</p>

                    <table class="table">
                        <thead>
                            <tr>
                                <th width="50%">Name</th>
                                <th ng-show="uploader.isHTML5">Size</th>
                                <th ng-show="uploader.isHTML5">Progress</th>
                                <th>Status</th>
                                <th>Actions</th>
                            </tr>
                        </thead>
                        <tbody>
                            <tr ng-repeat="item in uploader.queue">
                                <td><strong>{{ item.file.name }}</strong></td>
                                <td ng-show="uploader.isHTML5" nowrap>{{ item.file.size/1024/1024|number:2 }} MB</td>
                                <td ng-show="uploader.isHTML5">
                                    <div class="progress" style="margin-bottom: 0;">
                                        <div class="progress-bar" role="progressbar" ng-style="{ 'width': item.progress + '%' }"></div>
                                    </div>
                                </td>
                                <td class="text-center">
                                    <span ng-show="item.isSuccess"><i class="glyphicon glyphicon-ok"></i></span>
                                    <span ng-show="item.isCancel"><i class="glyphicon glyphicon-ban-circle"></i></span>
                                    <span ng-show="item.isError"><i class="glyphicon glyphicon-remove"></i></span>
                                </td>
                                <td nowrap>
                                    <button type="button" class="btn btn-success btn-xs" ng-click="item.upload()" ng-disabled="item.isReady || item.isUploading || item.isSuccess">
                                        <span class="glyphicon glyphicon-upload"></span> Upload
                                    </button>
                                    <button type="button" class="btn btn-warning btn-xs" ng-click="item.cancel()" ng-disabled="!item.isUploading">
                                        <span class="glyphicon glyphicon-ban-circle"></span> Cancel
                                    </button>
                                    <button type="button" class="btn btn-danger btn-xs" ng-click="item.remove()">
                                        <span class="glyphicon glyphicon-trash"></span> Remove
                                    </button>
                                </td>
                            </tr>
                        </tbody>
                    </table>

                    <div>
                        <div>
                            Queue progress:
                            <div class="progress" style="">
                                <div class="progress-bar" role="progressbar" ng-style="{ 'width': uploader.progress + '%' }"></div>
                            </div>
                        </div>
                        <button type="button" class="btn btn-success btn-s" ng-click="uploader.uploadAll()" ng-disabled="!uploader.getNotUploadedItems().length">
                            <span class="glyphicon glyphicon-upload"></span> Upload all
                        </button>
                        <button type="button" class="btn btn-warning btn-s" ng-click="uploader.cancelAll()" ng-disabled="!uploader.isUploading">
                            <span class="glyphicon glyphicon-ban-circle"></span> Cancel all
                        </button>
                        <button type="button" class="btn btn-danger btn-s" ng-click="uploader.clearQueue()" ng-disabled="!uploader.queue.length">
                            <span class="glyphicon glyphicon-trash"></span> Remove all
                        </button>
                    </div>

                </div>

            </div>

        </div>

    </body>
</html>