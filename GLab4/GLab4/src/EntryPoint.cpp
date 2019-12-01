#include <iostream>
#include <GL/glew.h>
#include <GL/freeglut.h>

// Rotation - right-hand-rule (rotation from right-to-left)
// The objects are dranw and their coordinate system is rotated
float angle = 0.0f;
const float angleStep = 2.0f;

// Positive Z-axis is going towards the screen
// The camera is at the origin and is looking towards the negative z-axis
float xPos = -9.0f;
bool xDirRight = true;
const float xStep = 0.2f;
const float xBound = 5.0f;

void displayCallback()
{
	// Clear the collor buffer & the depth buffer
	glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
	// Reset the parameters of the model-view matrix 
	glLoadIdentity();	

	// Translation
	glTranslatef(xPos, 0.0f, -10.0f);
	// Rotation
	glRotatef(angle, 1.0f, 1.0f, 1.0f);

	// GL_FLAT specifies that blending will not occur
	// GL_SMOOTH is the default
	glShadeModel(GL_SMOOTH);	

	// Draw calls
	// GL_QUADS - each 4 vertices form a quad
	glBegin(GL_QUADS);
	
	// Specify Vertices - anti-clockwise fashion for the front & clockwise fashion for the back 
	// Hard-coded cube vertex data
	//Front
	glColor3f(1.0, 0.0, 0.0);
	glVertex3f(-1.0,  1.0, 1.0);
	glVertex3f(-1.0, -1.0, 1.0);
	glVertex3f( 1.0, -1.0, 1.0);
	glVertex3f( 1.0,  1.0, 1.0);
	//Back
	glColor3f(0.0, 1.0, 0.0);
	glVertex3f( 1.0,  1.0, -1.0);
	glVertex3f( 1.0, -1.0, -1.0);
	glVertex3f(-1.0, -1.0, -1.0);
	glVertex3f(-1.0,  1.0, -1.0);
	//Right
	glColor3f(0.0, 0.0, 1.0);
	glVertex3f(1.0,  1.0,  1.0);
	glVertex3f(1.0, -1.0,  1.0);
	glVertex3f(1.0, -1.0, -1.0);
	glVertex3f(1.0,  1.0, -1.0);
	//Left
	glColor3f(1.0, 1.0, 0.0);
	glVertex3f(-1.0,  1.0, -1.0);
	glVertex3f(-1.0, -1.0, -1.0);
	glVertex3f(-1.0, -1.0,  1.0);
	glVertex3f(-1.0,  1.0,  1.0);
	//Top
	glColor3f(0.0, 1.0, 1.0);
	glVertex3f(-1.0, 1.0, -1.0);
	glVertex3f(-1.0, 1.0,  1.0);
	glVertex3f( 1.0, 1.0,  1.0);
	glVertex3f( 1.0, 1.0, -1.0);
	//Bottom
	glColor3f(1.0, 0.0, 1.0);
	glVertex3f(-1.0, -1.0, -1.0);
	glVertex3f(-1.0, -1.0,  1.0);
	glVertex3f( 1.0, -1.0,  1.0);
	glVertex3f( 1.0, -1.0, -1.0);

	glEnd();

	glutSwapBuffers();
}

// Called on window reshape
void reshapeCallback(int width, int height)
{
	// Set Viewport
	glViewport(0, 0, (GLsizei)width, (GLsizei)height);

	// Set Projection
	glMatrixMode(GL_PROJECTION);
	// Reset the parameters of the projection matrix
	glLoadIdentity();	 

	// FOV is usually 45 or 60 - the angle between the top-left & bottom-left corners of the projection (== top-right and bottom-right)
	// Aspect ratio
	// Z-near & Z-far - distance between the near clip-pane to the camera & the far clip-pane to the camera
	gluPerspective(60, 1, 2.0f, 50.0f);
	glMatrixMode(GL_MODELVIEW); 
}

void timer(int)
{
	// Call Display 60 times per second (OpenGL does it by itself)
	glutPostRedisplay();
	// Timer calls itself periodically - 60fps
	glutTimerFunc(1000 / 60, timer, 0);

	// Update X
	switch (xDirRight)
	{
	case true:
		if (xPos < xBound)
			xPos += xStep;
		else
			xDirRight = false;
		break;
	case false:
		if (xPos > -xBound)
			xPos -= xStep;
		else
			xDirRight = true;
		break;
	}
	// Update Angle (degrees)
	angle += angleStep;
	if (angle > 360.0f)
		angle -= 360.0f;
	std::cout << "Timer-Step: xPos=" << xPos << ", angle=" << angle << std::endl;
}

void initColor()
{
	// Color is a state variable in OpenGL
	// If Color = Value, then everything drawn will be drawn in Color (which is Value at the given point in time)
	// Colors are assigned per vertex
	// Example: To draw a cube with different colors for faces, the color must be set before drawing each face
	glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
	// Enablde depth-testing using z-buffering
	glEnable(GL_DEPTH_TEST);
}

int main(int argc, char** argv)
{
	glutInit(&argc, argv);
	// RGB & Double-Buffer display mode
	glutInitDisplayMode(GLUT_RGB | GLUT_DOUBLE | GLUT_DEPTH);

	glutInitWindowPosition(200, 100);
	glutInitWindowSize(500, 500);

	glutCreateWindow("Test");

	glutDisplayFunc(displayCallback);
	glutReshapeFunc(reshapeCallback);
	
	// Call a function after a specified ammount of time
	glutTimerFunc(10, timer, 0);

	initColor();

	glutMainLoop();
}
