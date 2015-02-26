<!DOCTYPE html>
<html>
	 <head>
	 	<meta charset="utf-8">
	 	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
		<title>locDev</title>

		 <?php
		 date_default_timezone_set('Europe/Lisbon');
                     
       //Get course info
		 $handle = @fopen("./course/meta-info", "r");
                     if($handle)
                     {
                         $course_info = fgets($handle);
			 $course_info = explode(";", $course_info);
                         while(($buffer = fgets($handle)) != false)
                         {
                             $shift = explode(";", $buffer);
                             $shifts[$shift[0]] = $shift;
                         }
                         unset($shift);
                         unset($buffer);
                      }
		     // check if the button was pressed
		     if(isset($_POST['register']))
		     {
		         if(!empty($_POST['sid']) and !empty($_POST['password']) and !empty($_POST['pid']))
			 {
			     if(strcmp($_POST['pid'], $course_info[3]) or strcmp(hash('md5',$_POST['password']), trim(strtolower($course_info[4])))) // professor authentication fails 
			     {
				 echo "<script>alert('Professor autentication failed!');</script>";
			     }
			     else
			     {
                                 $today = getdate();
                                 $format = './course/shifts/%s/%d-%d-%d-%d-%d/';
                                 $folder = sprintf($format,$_POST['shift'],$today['year'],$today['mon'],$today['mday'],$shifts[$_POST['shift']][4],$shifts[$_POST['shift']][5]);
			         $attendancesFile = $folder.'attendances.dat';
	                         if (file_exists($attendancesFile))
                                 {
                                     $content = ';'.$_POST['sid'];
                                 }
                                 else
                                 {
                                     $content = $_POST['sid'];
                                 }
                                 $ret = file_put_contents($attendancesFile,$content, FILE_APPEND | LOCK_EX);
                                 if ($ret > 0)
                                 {
                                     echo "<script>alert(\"Attendance confirmed!\")</script>";
                                 }
                                 else 
                                 {
                                     echo "<script>alert(\"Error in file_put_contents\")</script>";
                                 }
                                 unset($today,$content,$ret,$attendancesFile);
		            }
			 }
			 else
			 {
		             echo "<script>alert('All fields are required!');</script>";
			 }
		     }
		 ?>
	 </head>
	 <body bgcolor="#0ca3d2">
		 <table width="723" align="center" border="0" bgcolor="#CACAFF">
			 <td>
				 <h1 align="center"><font color="#000066">locDev - Automatic Presence Registration System</font></h1>
			 </td>
		 </table>
		 <table width="723" align="center" border="0" bgcolor= "#DFDFFF">
			 <td align="center"><a href="create.php"><font color="#0000FF">Create Account</font></a></td>
			 <td align="center"><a href="index.php"><font color="#0000FF">Confirm Attendance</font></a></td>
			 <td align="center"><a href="professor.php"><font color="#0055FF">Professor Area</font></a></td>
		 </table>
		 <table width="723" align="center" bgcolor="#FFFFFF">
			 <td>
				 <h3><font color="#000099">Register Student Attendance</font></h3>
				 <form method="post" action="professor.php">
					 <p>
					    	 <font color="#000099">Student's IDs (separated by ";") <input type="text" name="sid" value="" style="width:90%"></font>
					 </p>
					 <p> 
                              <h6><font color="#000099">IST ID MUST BE 55065, NOT ist55065</font></h6>
		                         </p>
                                         <p>
                                                 <font color="#000099">Shift : &nbsp;&nbsp;   
                                                 <select name="shift">
                                                 <?php
                                                     if(!empty($shifts))
                                                     {
                                                         foreach ($shifts as &$shift)
                                                         {
                                                             echo "<option value=$shift[0]>$shift[0], Type:$shift[2] Weekday:$shift[3] Time:$shift[4]h$shift[5] Room:$shift[6]</option>";
                                                         }
                                                     }
                                                 ?>
                                                  </select>
                                                  </font>
                                         </p>

					 <p>
						 <font color="#000099">Professor's ID : <input type="text" name="pid" value="" style="width:6%"></font>
					 </p>
					 <p>
						 <font color="#000099">Password : <input type="password" name="password" value=""></font>
					 </p>
					 <p> 
						 <h6><font color="#000099">All Fields are required.</font></h6>
					 </p>
					 <p>
						 <input type="submit" name="register" value="Register Attendance">
					 </p>
				 </form>
			 </td>
		</table>
	 </body>
</html>
