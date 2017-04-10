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
    Password    VARCHAR(32)  NOT NULL,
    FirstName   VARCHAR(32)  NOT NULL,
    LastName    VARCHAR(32)  NOT NULL,
    Score       INTEGER       ) ;
--
ALTER SEQUENCE Math.Competitor_ID_Seq RESTART WITH 101 ;
INSERT INTO Math.Competitor ( Username, Password, FirstName, LastName, Score )
VALUES
( 'Jeff',        'AppLE',   'Jeff',    'Solheim' , 0   ),
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
;

SELECT * FROM Math.Competitor ;
--
CREATE TABLE Math.Choice
  ( ID          SERIAL       PRIMARY KEY,
    ChoiceText  VARCHAR(256)  NOT NULL ) ;
--
ALTER SEQUENCE Math.Choice_ID_Seq RESTART WITH 201 ;
INSERT INTO Math.Choice ( ChoiceText )
VALUES ( '\( \dfrac {17} {12} \)' ) , -- 201
       ( '\( \dfrac {12} {17} \)' ) , -- 202
       ( '\( \dfrac {5} {7} \)' ) , -- 203
       ( '\( \dfrac {7} {5} \)' ) , -- 204
       ( '\( \dfrac {1-\sqrt {2}} {2} \)' ) , -- 205
       ( '\( \dfrac {2-\sqrt {3}} {2} \)' ) , -- 206
       ( '\( \dfrac {3-\sqrt {2}} {3} \)' ) , -- 207
       ( '\( False \)' ) , -- 208
       ( '\( True \)' ),   -- 209
	   
	   ( '99' ), -- 210
	   ( '100' ), -- 211
	   ( '101' ), -- 212
	   ( '102' ), -- 213
	   
	   ( '150' ), -- 214
	   ( '155' ), -- 215
	   ( '1500'), -- 216
	   ( '145' ), -- 217
	   
	   ( '2'), -- 218
	   ( '3'), --219
	   ( '\( \dfrac {1} {2} \)'), --220
	   ( '4' ), --221
	   
	   ( '\( \dfrac {1} {6} \)' ) , -- 222
	   ( '\( \dfrac {2} {3} \)' ) , -- 223
	   ( '\( \dfrac {5} {6} \)' ) , -- 224
	   ( '\( \dfrac {1} {4} \)' ) ,  -- 225
	   
	   ( '\( \dfrac {1} {2} \)' ) , -- 226
	   ( '\( \dfrac {1} {3} \)' ) , -- 227
	   -- 225 
	   -- 222
	   
	   ( '-59' ), -- 228
	   ( '-57' ), -- 229
	   ( '-69' ), -- 230
	   ( '-63' ), -- 231
	   
	   ( '132' ), --232
	   ( '142' ), --233
	   ( '144' ), --234
	   ( '122' ) --235
	   
	   
;
SELECT * FROM Math.Choice ;
--
CREATE TABLE Math.Question
  ( ID             SERIAL         PRIMARY KEY,
    QuestionText   VARCHAR(256)   NOT NULL,
    CorrectAnswer_Choice_ID  INTEGER REFERENCES Math.Choice(ID)  NOT NULL,
    Foil1_Choice_ID          INTEGER REFERENCES Math.Choice(ID)  NOT NULL,
    Foil2_Choice_ID          INTEGER REFERENCES Math.Choice(ID)  NULL,
    Foil3_Choice_ID          INTEGER REFERENCES Math.Choice(ID)  NULL,
    CHECK ( (Foil2_Choice_ID IS NOT NULL) OR (Foil3_Choice_ID IS NULL) ) ) ;
--
ALTER SEQUENCE Math.Question_ID_Seq RESTART WITH 301 ;
INSERT INTO Math.Question ( QuestionText, CorrectAnswer_Choice_ID, Foil1_Choice_ID, Foil2_Choice_ID, Foil3_Choice_ID )
VALUES ( '\( \dfrac {2} {3}+\dfrac {3} {4} \)', 201, 202, 203, 204 ) ; -- 301
--
INSERT INTO Math.Question ( QuestionText, CorrectAnswer_Choice_ID, Foil1_Choice_ID, Foil2_Choice_ID )
VALUES ( '\( \sin \left( \dfrac {\pi } {6}\right) -\cos \left( \dfrac {\pi } {4}\right) \)', 205, 206, 207 ) ; -- 302
--
INSERT INTO Math.Question ( QuestionText, CorrectAnswer_Choice_ID, Foil1_Choice_ID )
VALUES ( '\( \dfrac {3} {4}\in \left\{ x|12x^{2}+17x=-6\right\} \)', 208, 209 ) ; -- 303
--
INSERT INTO Math.Question ( QuestionText, CorrectAnswer_Choice_ID, Foil1_Choice_ID, Foil2_Choice_ID, Foil3_Choice_ID )
VALUES ( '57 + 44', 212 , 210, 211, 213 ) ; -- 304
--
INSERT INTO Math.Question ( QuestionText, CorrectAnswer_Choice_ID, Foil1_Choice_ID, Foil2_Choice_ID, Foil3_Choice_ID )
VALUES ( '15 x 10', 214, 215, 216, 217) ; -- 305
--
INSERT INTO Math.Question ( QuestionText, CorrectAnswer_Choice_ID, Foil1_Choice_ID, Foil2_Choice_ID, Foil3_Choice_ID )
VALUES ( '\( 20\div 10 \)', 218, 219, 220, 221) ; -- 306
--
INSERT INTO Math.Question ( QuestionText, CorrectAnswer_Choice_ID, Foil1_Choice_ID, Foil2_Choice_ID, Foil3_Choice_ID )
VALUES ( '\( \dfrac {1} {3}\times \dfrac {1} {2} \)', 222, 223, 224, 225) ; -- 307
--
INSERT INTO Math.Question ( QuestionText, CorrectAnswer_Choice_ID, Foil1_Choice_ID, Foil2_Choice_ID, Foil3_Choice_ID )
VALUES ( '\( \dfrac {1} {4} \div \dfrac {1} {2}  \)', 226, 227, 225, 222) ; -- 308
--
INSERT INTO Math.Question ( QuestionText, CorrectAnswer_Choice_ID, Foil1_Choice_ID, Foil2_Choice_ID, Foil3_Choice_ID )
VALUES ( '16 - 75 ', 228, 229, 230, 231) ; -- 309
--
INSERT INTO Math.Question ( QuestionText, CorrectAnswer_Choice_ID, Foil1_Choice_ID, Foil2_Choice_ID, Foil3_Choice_ID )
VALUES ( '12 x 11 ', 232, 233, 234, 235) ; -- 310


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
INSERT INTO Math.Submission ( Competitor_ID, Question_ID, AtTime, Selected_Choice_ID )
VALUES      ( 101, 302, '2017-02-28 14:47:54', 206 ),
            ( 103, 301, '2017-02-28 15:07:59', 203 ),
            ( 102, 303, '2017-02-28 15:07:59', 208 ) ;
SELECT * FROM Math.Submission ;
--
--\q
--
