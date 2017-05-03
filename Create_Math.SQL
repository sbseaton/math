-- 
-- Create_Math.SQL  by  JA Solheim  21 FEB 2017
-- This file contains a PostgreSQL script that,
-- when executed, creates the Math schema & tables.
-- 
DROP SCHEMA IF EXISTS Math CASCADE ;
CREATE SCHEMA Math ;
--
CREATE TABLE Math.Competitor
  ( ID          SERIAL       PRIMARY KEY,
    Username    VARCHAR(32)  NOT NULL,
	/* 
	Password    VARCHAR(32)  NOT NULL,
    FirstName   VARCHAR(32)  NOT NULL,
    LastName    VARCHAR(32)  NOT NULL,
	*/
    Score       INTEGER       ) ;
--
ALTER SEQUENCE Math.Competitor_ID_Seq RESTART WITH 1 ;

/* 

( 'Cooper',      'baNANa',  'Cooper',  'Cummings', 0   ),
( 'Zachery',     'grAPE',   'Zachery', 'Glass'   , 0   ),
( 'Taylor',      'LETTuce', 'Taylor',  'Gonzalez', 0   ),
( 'William',     'tomaTO',  'William', 'Goodrow' , 0   ),
( 'Kale',        'PLUm',    'Kale',    'Harrison', 0  ),
( 'Kurtis',      'cHERRy',  'Kurtis',  'Hass'    , 0   ),
( 'Zane',        'RADish',  'Zane',    'Heminway', 0   ),
( 'Adam',        'tURNip',  'Adam',    'Horn'    , 0  ),
( 'Jordan',      'SQUash',  'Jordan',  'Jara'    , 0   ),
( 'Gregory',     'pEA',     'Gregory', 'Kenyon'  , 0   ),
( 'Philip',      'CARrot',  'Philip',  'McCrickard', 0 ),
( 'Clinton',     'BeaN',    'Clinton', 'Petrie'    , 0 ),
( 'Samuel',      'manGO',   'Samuel',  'Seaton'  , 0   )

*/


SELECT * FROM Math.Competitor ;
--
CREATE TABLE Math.Choice
  ( ID          SERIAL       PRIMARY KEY,
    ChoiceText  VARCHAR(256)  NOT NULL ) ;
--
ALTER SEQUENCE Math.Choice_ID_Seq RESTART WITH 1 ;
INSERT INTO Math.Choice ( ChoiceText )
VALUES ('\( 64 \)' ) , -- 1
       ( '\( 54 \)' ) , -- 2
	   
       ( '\( 4096 \)' ) , -- 3
       ('\( 5144 \)' ) , -- 4
	   
       ( '\( False \)' ) , -- 5
       ( '\( True \)' ) , -- 6
	   
       ( '\( True \)' ) , -- 7
       ( '\( False \)' ) , -- 8
	   
       ( '\( 13 \)' ),   -- 9
	   ( '\( 11 \)' ), -- 10
	   
	   ( '\( 22 \)' ), -- 11
	   ( '\( 86 \)' ), -- 12
	   ( '\( 16 \)' ), -- 13
	   
	   ( '\( 2 \)' ), -- 14
	   ( '\( 168 \)' ), -- 15
	   ( '\( 178 \)' ), -- 16
		
		
	   ( '\( -11 \)' ), --17
       ( '\( -9 \)' ), --18
       ( '\( 1 \)'  ), --19
       ( '\( \sqrt {3} \)'  ), -- 20
       ( '\( 3596 \)'  ), -- 21
       ( '\( 3476 \)'  ), -- 22
       ( '\( 3582 \)'  ), -- 23
       ( '\( -7 \)'  ), --24
       ( '\( -\dfrac {1} {7} \)'  ), --
       ( '\( -\dfrac {2} {7} \)'  ),
       ( '\( -2 \)'  ),
       ( '\( -3 \)'  ),
       ( '\( -4 \)'  ),
       ( '\( \left( \dfrac {4} {3},\infty \right)  \)'  ),
       ( '\( \left( -\infty ,-\dfrac {3} {4}\right)  \)'  ),
       ( '\( \left( \dfrac {5} {3},\infty \right)  \)'  ),
       ( '\( -4 \)'  ),
       ( '\( \dfrac {3\pi } {2} \)'  ),
       ( '\( -2\pi  \)'  ),
       ( '\( 3\dfrac {5} {6} \)'  ),
       ( '\( 5\dfrac {1} {3} \)'  ),
       ( '\( 4\dfrac {1} {6} \)'  ),
       ( '\( 57 \)'  ),
       ( '\( 61 \)'  ),
       ( '\( 73 \)'  ),
       ( '\( \dfrac {3} {2} \)'  ),
       ( '\( \dfrac {5} {3} \)'  ),
       ( '\( 1\dfrac {5} {6} \)'  ),
       ( '\( 9996 \)'  ),
       ( '\( 9656 \)'  ),
       ( '\( 10056 \)'  ),
       ( '\( \left( 5,4\right)  \)'  ),
       ( '\( \left( 4,5\right)  \)'  ),
       ( '\( \left( 3,6\right)  \)'  ),
       ( '\( \dfrac {4\pi } {3} \)'  ),
       ( '\( \dfrac {5\pi } {3} \)'  ),
       ( '\( \dfrac {7\pi } {6} \)'  ),
       ( '\( \dfrac {11\pi } {6} \)'  ),
       ( '\( 496 \)'  ),
       ( '\( 504 \)'  ),
       ( '\( 488 \)'  ),
       ( '\( 484 \)'  ),
       ( '\( 2x\cdot \cos \left( x^{2}\right) \)'  ),
       ( '\( 2\cdot \cos \left( 2x\right) \)'  ),
       ( '\( -2x\cdot \cos \left( 2x^{2}\right) \)'  ),
       ( '\( 4x\cdot \cos \left( 2x\right)  \)'  ),
       ( '\( 2331 \)'  ),
       ( '\( 2341 \)'  ),
       ( '\( 2351 \)'  ),
       ( '\( 2361 \)'  ),
       ( '\( 120 \)'  ),
       ( '\( 360 \)'  ),
       ( '\( 240 \)'  ),
       ( '\( 180 \)'  ),
       ( '\( 111001 \)'  ),
       ( '\( 110101 \)'  ),
       ( '\( 101011 \)'  ),
       ( '\( 110111 \)'  ),
       ( '\( 40320 \)'  ),
       ( '\( 38760 \)'  ),
       ( '\( 42240 \)'  ),
       ( '\( 43180 \)'  ),
       ( '\( 1 \)'  ),
       ( '\( -2 \)'  ),
       ( '\( -1 \)'  ),
       ( '\( 2 \)'  ),
       ( '\( 792 \)'  ),
       ( '\( 786 \)'  ),
       ( '\( 812 \)'  ),
       ( '\( 772 \)'  ),
       ( '\( \dfrac {5} {18} \)'  ),
       ( '\( \dfrac {9} {36} \)'  ),
       ( '\( \dfrac {7} {18} \)'  ),
       ( '\( \dfrac {11} {36} \)'  ) ;
	     

SELECT * FROM Math.Choice ;
--
CREATE TABLE Math.Question
  ( ID             SERIAL         PRIMARY KEY,
	PointValue		   INTEGER, 
    QuestionText   VARCHAR(256)   NOT NULL,
    CorrectAnswer_Choice_ID  INTEGER REFERENCES Math.Choice(ID)  NOT NULL,
    Foil1_Choice_ID          INTEGER REFERENCES Math.Choice(ID)  NOT NULL,
    Foil2_Choice_ID          INTEGER REFERENCES Math.Choice(ID)  NULL,
    Foil3_Choice_ID          INTEGER REFERENCES Math.Choice(ID)  NULL,
    CHECK ( (Foil2_Choice_ID IS NOT NULL) OR (Foil3_Choice_ID IS NULL) ) ) ;
--
ALTER SEQUENCE Math.Question_ID_Seq RESTART WITH 1 ;
INSERT INTO Math.Question ( PointValue, QuestionText, CorrectAnswer_Choice_ID, Foil1_Choice_ID )
VALUES  ( 2, '\( 0.4\times 160 \)', 1, 2 ),
        ( 2, '\( 2^{12} \)', 3, 4 ),
	    ( 2, '\( e^{2}>9 \)', 5, 6 ),
	    ( 2, '\( 3\ |\ 85203 \)', 7, 8 ),
	    ( 2, '\( 8x-3=101 \)', 9, 10 ),
	    ( 2, '\( \left| \begin{matrix} 6& 4\\ 8& 9\end{matrix} \right|  \)', 11, 12 ),
	    ( 2, '\( x>0\ \land \left( x-7\right) ^{2}=81 \)', 13, 14 ),
	    ( 2, '\( 35\%\ \ of\ \ 480 \)', 15, 16 ),
	    ( 2, '\( f\left( 3\right)\ \ \text{if}\ \ f\left( x\right) =2x^{2}-10x+1 \)', 17, 18 ),
	    ( 2, '\( \sin \left( \dfrac {\pi } {6}\right) +\cos \left( \dfrac {\pi } {3}\right)  \)', 19, 20 ) ;

INSERT INTO Math.Question ( PointValue, QuestionText, CorrectAnswer_Choice_ID, Foil1_Choice_ID, Foil2_Choice_ID )
VALUES 	   
		( 3, '\( 58\times 62 \)', 21, 22, 23 ),
		( 3, '\( m\ \ \text{if}\ \ P_{1}=\left( 16,18\right) \land P_{2}=\left( 9,60\right)  \)', 24, 25, 26 ),
		( 3, '\( x\ \lt\ 0\ \land \left| 2x-1\right| =5 \)', 27, 28, 29 ),
		( 3, '\( 5-12y\ \lt\ -11 \)', 30, 31, 32 ),
		( 3, '\( \int _{\pi }^{2\pi }2\sin \left( x\right) dx \)', 33, 34, 35 ),
		( 3, '\( \int _{1}^{2}\left( x^{2}+x\right) dx \)', 36, 37, 38 ),
		( 3, '\( f\text{&prime;}\left( -4\right)  \ \text{if}\ \ f\left( x\right) =x^{3}+x^{2}+x \)', 39, 40, 41 ),
		( 3, '\( x>1\dfrac {1} {3}\ \ \land\ \ 8x^{2}-22x+15=0 \)', 42, 43, 44 ),
		( 3, '\( 34\times 294 \)', 45, 46, 47 ),
		( 3, '\( \begin{align*} & 2x-3y=-2\\ & 4x+y=24\end{align*} \)', 48, 49, 50 ) ;

INSERT INTO Math.Question ( PointValue, QuestionText, CorrectAnswer_Choice_ID, Foil1_Choice_ID, Foil2_Choice_ID, Foil3_Choice_ID )
VALUES 		
		( 4, '\( \text{Least positive &theta; &SuchThat; }\ 2\sin \left( \theta \right) =-\sqrt {3} \)', 51, 52, 53, 54 ),
		( 4, '\( \log _{10}\left( 2s+8\right) =3 \)', 55, 56, 57, 58 ),
		( 4, '\( f\text{&prime;}\left( x\right)  \ \text{if}\ \ f\left( x\right) =\sin \left( x^{2}\right)  \)', 59, 60, 61, 62 ),
		( 4, '\( 37\times 63 \)', 63, 64, 65, 66 ),
		( 4, '\( \left( \begin{matrix} 10\\ 3\end{matrix} \right)  \)', 67, 68, 69, 70 ),
		( 4, '\( \text{57 in binary} \)', 71, 72, 73, 74 ),
		( 4, '\( 8! \)', 75, 76, 77, 78 ),
		( 4, '\( \text{Least x &SuchThat; }\ \log _{2}\left( x+1\right) +\log _{2}\left( x\right) =1 \)', 79, 80, 81, 82 ),
		( 4, '\( \dfrac {k} {594}=\dfrac {4} {3} \)', 83, 84, 85, 86 ),
		( 4, '\( \text{P(doubles or sum=5) if two dice rolled} \)', 87, 88, 89, 90 ) ;
--


SELECT * FROM Math.Question ;
--
CREATE TABLE Math.Submission
	(
	Competitor_ID       INTEGER REFERENCES Math.Competitor(ID)  NOT NULL,
	Question_ID         INTEGER REFERENCES Math.Question(ID)    NOT NULL,
	AtTime              TIMESTAMP                               NOT NULL,
	Selected_Choice_ID  INTEGER REFERENCES Math.Choice(ID)      NOT NULL, 
	PRIMARY KEY ( Competitor_ID, Question_ID ) 
	) ;	
--
 /* INSERT INTO Math.Submission ( Competitor_ID, Question_ID, AtTime, Selected_Choice_ID )
VALUES      ( 101, 302, '2017-02-28 14:47:54', 206 ),
            ( 103, 301, '2017-02-28 15:07:59', 203 ),
            ( 102, 303, '2017-02-28 15:07:59', 208 ) ; */
SELECT * FROM Math.Submission ;
--
--\q
--
