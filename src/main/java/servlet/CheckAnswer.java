
import java.io.IOException ;
import java.io.PrintWriter ;
import javax.servlet.ServletException ;
import javax.servlet.http.HttpServlet ;
import javax.servlet.http.HttpServletRequest ;
import javax.servlet.http.HttpServletResponse ;
import javax.servlet.http.HttpSession ;
import java.util.Random ;

public class CheckAnswer extends HttpServlet
	{

	@Override
	protected void doPost (	HttpServletRequest  request,
							HttpServletResponse response )
		throws ServletException, IOException
		{
		int questionNumber = (Integer)request.getParameter( "questionNumber" );
		String usersChoice   = request.getParameter ( "choice" ) ;

		String correctChoiceQuery = "select * "	
								+ "from math.choice "
								+ "inner join math.question "
								+ "on (choice.id = question.correctanswer_choice_id)  "
								+ "where question.id =" + questionNumber + " " ;

		ResultSet correctChoice = statement.executeQuery ( correctChoiceQuery ) ;
		
		response.setContentType ( "text/html" ) ;
		PrintWriter out = response.getWriter() ;

        try
            {
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
			if ( usersChoice .equals (correctChoice) )
				out.println ( "	   <h1>Your answer was correct.</h1>" ) ;
			else
				out.println ( "	   <h1>Your answer was not correct.</h1>" ) ;
            out.println ( "	   <hr>" ) ;
            out.println ( "  </body>" ) ;
            out.println ( "</html>" ) ;
			} // end try block
		finally	{ out.close() ; }
		} // end doPost method

	} // CheckAnswer class
