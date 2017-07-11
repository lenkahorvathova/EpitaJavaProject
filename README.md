# EpitaJavaProjectIAM
Solution to the IAM project for EPITA's Java course.
<br /><br />
For proper functioning of this IAM-core project, please, import also a project 'EpitaJavaProjectLOGGIN'.<br />
For every launch of the program, you can find logs in 'temp/application.log' file.<br />
Before starting the program, import the project to the Eclipse and start up the Derby database.<br />
Then, use a provided SQL for creating and filling the database and put a TXT file in the specified 'temp/' directory.<br />
Now, you can start the program by launching a 'Launcher' class.<br />
First, you need to authenticate yourself as an admin, with a username 'adm' and a password 'pwd'.<br />
Otherwise, you are denied an access and a session closes.<br />
After successful authentication, you are free to manage the database of identities.<br />
At the beginning, you need to choose a preferred version, DAO working with a file or a database.<br />
You can see identities you are currently working with.<br />
You are given an option to reset an example set of identities, if you don't like changes you made.<br />
After that, you can create, update or delete identities until you decide to quit the program.<br />
When creating an identity, you are asked for a name, email and if you are working with a file, also for a UID.<br />
When updating an identity, you have to know its specific UID and then you are asked to update its name and email.<br />
When deleting an identity, again, you have to know its UID and the corresponding identity will be removed.<br />
Last option is to end your session.<br />
You can find all the identities either in a '/temp/tests/identities.txt' file or in a IDENTITIES table in an IAM scheme.<br />
