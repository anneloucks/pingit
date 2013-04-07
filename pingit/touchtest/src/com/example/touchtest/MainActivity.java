package com.example.touchtest;

import android.os.Bundle;
import android.app.Activity;
import android.graphics.Color;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RelativeLayout;


public class MainActivity extends Activity implements View.OnClickListener {

	int gridWidth = 10;
	int gridHeight = 10;
	int gridCellSize = 30;
	int numTargets = 4;
	int[] targetX;
	int[] targetY;
	
	Button[] myButtons;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		//Arrange our target locations
		targetX = new int[numTargets];
		targetY = new int[numTargets];

		for( int i = 0; i < numTargets; i++)
		{
		    targetX[i] = (int) (Math.random() * gridWidth);
		    targetY[i] = (int) (Math.random() * gridHeight);
		}

		//Make a pile of buttons, which will be our grid.
		RelativeLayout myLayout = (RelativeLayout)this.findViewById(R.id.mainRelativeLayout);
		myButtons = new Button[gridWidth * gridHeight];

		//Arrange all the buttons
		for( int i = 0; i < gridWidth * gridHeight; i++ )
		{
	        RelativeLayout.LayoutParams newLayoutParameters = 
	        		new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
	        				                        RelativeLayout.LayoutParams.MATCH_PARENT);
	        newLayoutParameters.alignWithParent = true;
	        newLayoutParameters.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
	        newLayoutParameters.addRule(RelativeLayout.ALIGN_PARENT_TOP);
	        newLayoutParameters.leftMargin = (i % gridWidth) * gridCellSize;
	        newLayoutParameters.topMargin = (i / gridHeight) * gridCellSize;
	        newLayoutParameters.height = gridCellSize;
	        newLayoutParameters.width = gridCellSize;        

	        myButtons[i] = new Button(this);
 			myButtons[i].setText("X");
 			myButtons[i].setTextSize(10);
 			myButtons[i].setId(i);
 			myButtons[i].setBackgroundColor(Color.CYAN);
 			myButtons[i].setLayoutParams(newLayoutParameters);
 			myButtons[i].setOnClickListener(this);
 			
 			myLayout.addView(myButtons[i]);
		}		
	}

	public boolean hitTest(int xGridPos, int yGridPos)
	{
    	for( int i = 0; i < numTargets; i++)
	    {
	        if( (xGridPos == targetX[i]) && (yGridPos == targetY[i]) )
	        {
			    return true;
	        }
	    }
    	return false;
	}

	@Override
	public void onClick( View myView)
	{
		//When the user taps one of the grid buttons, this happens
        boolean isHit = false;
        int numContacts = 0;
		int xGridPos = myView.getId() % 10;
		int yGridPos = myView.getId() / 10;
		
        //Set the background to Yellow, because that is a good
		//default
		
		//Check to see if we are in blacking mode
		CheckBox blackingCheckBox = (CheckBox)this.findViewById(R.id.blackingCheckBox);
		if( true == blackingCheckBox.isChecked() )
		{
			myView.setBackgroundColor(Color.BLACK);
		}  else
		{
    		myView.setBackgroundColor(Color.YELLOW);
	
			//Check to see if it is a hit, and if so make the cell red
			if( true == hitTest( xGridPos, yGridPos ) )
			{
				myView.setBackgroundColor(Color.RED);
				isHit = true;
			}
		
			//If we are not a hit, then we need to calculate a number for the cell
			if( isHit == false )
			{
				//Starting at our location fire the sonar in each direction
				//(including diagonal)
				int shotX = xGridPos;
				int shotY = yGridPos;
				
				//I admit this all seems a little excessive, and there is probably a better way
				//to do it, but it is a good way to ensure that each direction is covered and that
				//overlaps don't happen
				
				//up
				shotX = xGridPos;
				shotY = yGridPos;
				while( (shotY > 0) && (false == hitTest(shotX, shotY)))
				{
					shotY--;
					if( true == hitTest(shotX, shotY) )
					{
						numContacts++;
					}
				}
				
				//down
				shotX = xGridPos;
				shotY = yGridPos;
				while( (shotY < (gridHeight - 1)) && (false == hitTest(shotX, shotY)))
				{
					shotY++;
					if( true == hitTest(shotX, shotY) )
					{
						numContacts++;
					}
				}
				
				//left
				shotX = xGridPos;
				shotY = yGridPos;
				while( (shotX > 0) && (false == hitTest(shotX, shotY)))
				{
					shotX--;
					if( true == hitTest(shotX, shotY) )
					{
						numContacts++;
					}
				}
	
				//right
				shotX = xGridPos;
				shotY = yGridPos;
				while( (shotX < (gridWidth - 1)) && (false == hitTest(shotX, shotY)))
				{
					shotX++;
					if( true == hitTest(shotX, shotY) )
					{
						numContacts++;
					}
				}
	
				//up Left
				shotX = xGridPos;
				shotY = yGridPos;
				while( (shotY > 0) && (shotX > 0) && (false == hitTest(shotX, shotY)))
				{
					shotY--;
					shotX--;
					if( true == hitTest(shotX, shotY) )
					{
						numContacts++;
					}
				}
	
				//up Right
				shotX = xGridPos;
				shotY = yGridPos;
				while( (shotY > 0) && (shotX < (gridWidth - 1)) && (false == hitTest(shotX, shotY)))
				{
					shotY--;
					shotX++;
					if( true == hitTest(shotX, shotY) )
					{
						numContacts++;
					}
				}
	
				//down Left
				shotX = xGridPos;
				shotY = yGridPos;
				while( (shotY < (gridHeight - 1)) && (shotX > 0) && (false == hitTest(shotX, shotY)))
				{
					shotY++;
					shotX--;
					if( true == hitTest(shotX, shotY) )
					{
						numContacts++;
					}
				}
	
				//down Right
				shotX = xGridPos;
				shotY = yGridPos;
				while( (shotY < (gridHeight - 1)) && (shotX < (gridWidth - 1)) && (false == hitTest(shotX, shotY)))
				{
					shotY++;
					shotX++;
					if( true == hitTest(shotX, shotY) )
					{
						numContacts++;
					}
				}
	
				//Now output the number we found for the cell into the button
				((Button)myView).setText(String.valueOf(numContacts));
			}
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
