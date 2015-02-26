<!DOCTYPE html>
<html>
	 <head>
		 <meta charset="utf-8">
		 <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
		 <title>locDev</title>

		 <?php
		     if(isset($_POST['create']))
		     {
		         if(!empty($_POST['id']) and !empty($_POST['password']) and !empty($_POST['confirm_password']))
			 {
			     if(file_exists('students/'.$_POST['id']))
			     {
				 echo "<script>alert('Student already exists!');</script>";
			     }
			     else
			     {
			         if(strcmp($_POST['password'], $_POST['confirm_password']) == 0)
				     {
				         file_put_contents('students/'.$_POST['id'], hash('md5',$_POST['password']));
				         echo "<script>alert('Account created!');</script>";
				     }
				     else
				     {
				         echo "<script>alert('Passwords don't match!');</script>";
				     }

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
			 <td align="center"><a href="index.php"><font color="#0055FF">Confirm Attendance</font></a></td>
			 <td align="center"><a href="professor.php"><font color="#0055FF">Professor Area</font></a></td>
		 </table>
		 <table width="723" align="center" bgcolor="#FFFFFF">
			 <td>
				 <h3><font color="#000099">Create new account</font></h3>
				 <form method="post" action="create.php">
					 <p>
					    	 <font color="#000099">IST's ID : <input type="text" name="id" value="" style="width:6%"></font>
					 </p>
					 <p> 
                                                 <h6><font color="#000099">IST ID MUST BE 55065, NOT ist55065</font></h6>
		                         </p>

					 <p>
						 <font color="#000099">Password : <input type="password" name="password" value=""></font>
					 </p>
					 <p>
						 <font color="#000099">Confirm Password : <input type="password" name="confirm_password" value=""></font>
					 </p>
					 <p> 
						 <h6><font color="#000099">All Fields are required.</font></h6>
					 </p>
					 <p>
						 <input type="submit" name="create" value="Create Account">
					 </p>
				 </form>
			 </td>
		</table>
	 </body>
</html>
