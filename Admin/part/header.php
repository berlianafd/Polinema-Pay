<?php
session_start();
?>
<!DOCTYPE html>
<html>
<head>
  <meta charset="utf-8">
  <meta http-equiv="X-UA-Compatible" content="IE=edge">
  <meta content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" name="viewport">
  <title>POLINEMA PAY</title>
  <link rel="stylesheet" href="assets/plugins/jQueryUI/jquery-ui.css">
  <link rel="stylesheet" href="assets/bootstrap/css/bootstrap.min.css">
  <link rel="stylesheet" href="assets/dist/css/AdminLTE.min.css">
  <link rel="stylesheet" href="assets/plugins/datatables/dataTables.bootstrap.css">
  <link rel="stylesheet" href="assets/plugins/morris/morris.css">
  <link rel="stylesheet" href="assets/dist/css/font-awesome.min.css">
  <link rel="stylesheet" href="assets/dist/css/ionicons.min.css">
  <link rel="stylesheet" href="assets/dist/css/skins/_all-skins.min.css">
  <link rel="stylesheet" href="assets/plugins/select2/select2.min.css">
  <link rel="stylesheet" href="assets/plugins/daterangepicker/daterangepicker-bs3.css">
  <link rel="stylesheet" href="assets/plugins/datepicker/datepicker3.css">
  <link rel="stylesheet" href="assets/plugins/iCheck/all.css">
  <link rel="stylesheet" href="assets/plugins/colorpicker/bootstrap-colorpicker.min.css">
  <link rel="stylesheet" href="assets/plugins/timepicker/bootstrap-timepicker.min.css">
  <link rel="stylesheet" href="assets/plugins/sweet-alert2/sweetalert2.min.css">
  <script src="assets/plugins/jQuery/jQuery-2.2.0.min.js"></script>
  <script src="assets/bootstrap/js/bootstrap.min.js"></script>
  <script src="assets/plugins/sweet-alert2/sweetalert2.min.js"></script>
  <style>
    .example-modal .modal {
      position: relative;
      top: auto;
      bottom: auto;
      right: auto;
      left: auto;
      display: block;
      z-index: 1;
    }
    .example-modal .modal {
      background: transparent !important;
    }
    .box.collapsed-box .box-body, .box.collapsed-box .box-footer {
      display: none;
    }
    li.dropdown {
      display: inline-block;
    }

    .dropdown:hover .isi-dropdown {
      display: inline-block;
      color: white;
      text-align: left;
      padding: 14px 16px;
      text-decoration: none;

    }

    .isi-dropdown a:hover {
      color: #fff !important;
    }

    .isi-dropdown {
      float: left;
      position: absolute;
      display: none;
      box-shadow: 0px 8px 16px 0px rgba(0,0,0,0.2);
      z-index: 1;
      background-color: #f9f9f9;
    }

    .isi-dropdown a {
      color: #3c3c3c !important;
    }

    .isi-dropdown a:hover {
      color: #232323 !important;
      background: #f3f3f3 !important;
    }
    .dropdown:hover .isi-dropdown {
      display: block;
    }
  </style>
  <script src="assets/plugins/jQuery/jQuery-2.2.0.min.js"></script>
  <script src="assets/plugins/highchart/highcharts.js"></script>
</head>
<body style="padding-top:50px; background-color: rgb(242, 244, 245)">
  <nav class="">
    <div class="container">
      <div id="navbar" class="navbar-brand  navbar-fixed-top">
        <ul class="nav navbar-nav navbar-right">
          <?php
          error_reporting(0);
          if (!empty($_SESSION['tipe'] == 'admin')) {
            echo "
            <li>
            <a href='jemputsampah.php' style='color: #00000f'>
            <i class='fa fa-home'></i>Dashboard</a>
            </li>";
            
          }else if (!empty($_SESSION['tipe'] == '')){
            echo"
            <li>
            <a href='jemputsampah.php' style='color: #00000f'>
            <i class='fa fa-home'></i> Home</a>
            </li>";
            
          }else{
            echo"
            <li>
            <a href='jemputsampah.php' style='color: #00000f'>
            <i class='fa fa-home'></i>Dashboard</a>
            </li>";
          }
          ?>
        </ul>
      </div>
    </div>
  </nav>
  
  <!-- Modal Login -->
  <div class="modal fade" id="modal-login" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
    <div class="modal-dialog modal-lg" role="document">
      <div class="modal-content">
        <div class="modal-body">
          <form class="form-horizontal" method="post" enctype="multipart/form-data" action="fungsi/login.php">
            <div class="box-header">
              <div class="box-footer" style=" border-top-left-radius: 0;
              border-top-right-radius: 0;
              border-bottom-right-radius: 3px;
              border-bottom-left-radius: 3px;
              border-top: 1px solid #f4f4f4;
              padding: 10px;
              background-color: rgb(‎204, 196, 193);
              box-shadow: 0px 3px 1px grey;">
              <h4 class="pull-left" style="color: #00000f;"><i class="fa fa-sign-in"></i> Login</h4>
            </div>
          </div>
          <div class="box-body" style="background-color: rgb(204, 196, 193);">
            <div class="row">
              <div class="col-sm-6 col-sm-offset-3">
                <div class="login-logo">
                  <a href=""><b>LOGIN</b></a>
                </div>
                <!-- /.login-logo -->
                <div class="form-group has-feedback">
                  <input type="username" class="form-control" placeholder="Username" name="email" required="">
                  <span class="glyphicon glyphicon-envelope form-control-feedback"></span>
                </div>
                <div class="form-group has-feedback">
                  <input type="password" class="form-control" placeholder="Password" name="password" required="">
                  <span class="glyphicon glyphicon-lock form-control-feedback"></span>
                </div>
                <div class="row">
                  <div class="col-xs-8">
                  </div>
                  <!-- /.col -->
                  <div class="col-xs-4">
                    <button type="submit" name="login" class="btn site-btn btn-block btn-flat"><i class="fa fa-sign-in"></i> Log In</button>
                  </div>
                  <!-- /.col -->
                </div>
                <!-- /.login-box-body -->
              </div>
            </div>
          </div>
        </form>
      </div>
    </div>
  </div>
</div>
</div>
</body>
</html>
<?php
//Fungsi menampilkan pesan ketika action berjalan
if(isset($_GET['pesan'])){
  $pesan = $_GET['pesan'];
  if($pesan == "input"){
    echo "<script>
    swal({
      position: 'top-end',
      type: 'success',
      title: 'Data Berhasil Disimpan',
      showConfirmButton: false,
      timer: 2000
      })
      </script>";
    }else if($pesan == "update"){
      echo "<script>
      swal({
        position: 'top-end',
        type: 'success',
        title: 'Data Berhasil Diupdate',
        showConfirmButton: false,
        timer: 2000
        })
        </script>";
      }else if($pesan == "hapus"){
        echo "<script>
        swal({
          position: 'top-end',
          type: 'success',
          title: 'Data Berhasil Dihapus',
          showConfirmButton: false,
          timer: 2000
          })
          </script>";
        }elseif ($pesan == "gagal") {
          echo "<script>
          swal({
            position: 'top-end',
            type: 'warning',
            title: 'Data Gagal Disimpan',
            showConfirmButton: false,
            timer: 2000
            })
            </script>";
          }elseif ($pesan == "login_success") {
            echo "<script>
            swal({
              position: 'top-end',
              type: 'success',
              title: 'Anda Berhasil Login',
              showConfirmButton: false,
              timer: 2000
              })
              </script>";
            }elseif ($pesan == "error_auth") {
              echo "<script>
              swal({
                position: 'top-end',
                type: 'warning',
                title: 'Username Atau Password Salah',
                showConfirmButton: false,
                timer: 2000
                })
                </script>";
              }
            }
            ?>
            <?php include'footer.php'; ?>
