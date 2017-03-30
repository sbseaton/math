
import java.io.IOException ;
import java.io.PrintWriter ;

/*import javax.servlet.ServletException ;
import javax.servlet.http.HttpServlet ;
import javax.servlet.http.HttpServletRequest ;
import javax.servlet.http.HttpServletResponse ;
import javax.servlet.http.HttpSession ;
import java.util.Random ;
*/
import  java.io.* ;
import  javax.servlet.* ;
import  javax.servlet.http.* ;
import  java.util.* ;
import  java.sql.* ;
import  javax.servlet.annotation.WebServlet ;

public class CheckAnswer extends HttpServlet
	{

	@Override
	protected void doPost (	HttpServletRequest  request,
							HttpServletResponse response )
		throws ServletException, IOException
		{
		 Connection connection     =  null ;	// added
 		 Statement  statement      =  null ;	// added
		final String DB_URL       =  System.getenv ( "JDBC_DATABASE_URL" ) ;
		
		response.setContentType ( "text/html" ) ;
		PrintWriter out = response.getWriter() ;

        try
            {
            connection = DriverManager.getConnection ( DB_URL ) ;
    		statement = connection.createStatement() ;
            int questionNumber = Integer.parseInt(request.getParameter( "questionNumber" ));
			String usersChoiceID   = request.getParameter ( "choice" ) ;
			String usersChoiceText = "";
			String correctChoiceID = "" ; 
			String correctAnswerText = "";

			String correctChoiceQuery = "select * "	
								+ "from math.choice "
								+ "inner join math.question "
								+ "on (choice.id = question.correctanswer_choice_id)  "
								+ "where question.id =" + questionNumber + " " ;

			ResultSet correctChoice = statement.executeQuery ( correctChoiceQuery ) ;

			if (correctChoice.next() )
			{
				correctChoiceID = "" + correctChoice.getObject("ID");
				correctChoiceText = "" + correctChoice.getObject("ChoiceText"); 
			}

            connection = DriverManager.getConnection ( DB_URL ) ;	// added
    		statement = connection.createStatement() ;				// added

            out.println ( "<!DOCTYPE html>" ) ;
            out.println ( "<html>" ) ;
            out.println ( "  <head> " ) ;
            out.println ( "    <meta charset = 'utf-8'>" ) ;
            out.println ( "    <title>TITLE</title>" ) ;
            out.println ( "    <script type='text/javascript' async" ) ;
            out.println ( "      src='https://cdn.mathjax.org/mathjax/latest/MathJax.js?config=TeX-MML-AM_CHTML'>" ) ;
            out.println ( "    </script>" ) ;
            out.println ( "  </head>" ) ;
            out.println ( "  <body>" ) ;
            out.println ( "    <hr>" ) ;
            out.println ( " <p> This is what the user chose:" + usersChoiceText + " <p> ");
            out.println ( " <p> This is what is expected: " + correctChoiceText + " <p> ");
			if ( usersChoiceText .equals (correctChoiceText) )
				out.println ( "	   <h1>Your answer was correct.</h1>" ) ;
			else
				out.println ( "	   <h1>Your answer was not correct.</h1>" ) ;
            out.println ( "	   <hr>" ) ;
            out.println ( "  </body>" ) ;
            out.println ( "</html>" ) ;
			} // end try block

		catch ( SQLException sqlException ) 
    	{
    		System.out.println ( "Caught SQLException ..." ) ;
    		sqlException.printStackTrace() ;
    		System.exit ( 1 ) ;
    	} // end catch
		finally	{ out.close() ; }
		} // end doPost method

	} // CheckAnswer class
