#include <iostream>
#include <GL/glew.h>
#include <GL/freeglut.h>

float xPos = -10;
bool xDirRight = true;
const float xStep = 0.2;

void displayCallback()
{
	glClear(GL_COLOR_BUFFER_BIT);
	glLoadIdentity();	// Reset the parameters of the model-view matrix 

	// Draw calls
	glBegin(GL_POLYGON);
	
	// Specify Vertices - anti-clockwise fashion
	glVertex2f(xPos,	   1.0f);
	glVertex2f(xPos,	  -1.0f);
	glVertex2f(xPos+2.0f, -1.0f);
	glVertex2f(xPos+2.0f,  1.0f);

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
	glLoadIdentity();	// Reset the parameters of the projection matrix 

	gluOrtho2D(-10, 10, -10, 10);
	glMatrixMode(GL_MODELVIEW); 
}

void timer(int)
{
	// Also call Display 60 times in 1 second - OpenGL does it by itself
	glutPostRedisplay();
	// Timer calls itself periodically - 60fps
	glutTimerFunc(1000 / 60, timer, 0);

	// BUGGED HERE
	// Update X
	switch (xDirRight)
	{
	case true:
		if (xPos < 8)
			xPos += xStep;
		else
			xDirRight = false;
		break;
	case false:
		if (xPos > -10)
			xPos -= xStep;
		else
			xDirRight = true;
		break;
	}
	std::cout << "Timer-Step: xPos=" << xPos << std::endl;
}

int main(int argc, char** argv)
{
	glutInit(&argc, argv);
	// RGB & Double-Buffer display mode
	glutInitDisplayMode(GLUT_RGB | GLUT_DOUBLE);

	glutInitWindowPosition(200, 100);
	glutInitWindowSize(500, 500);

	glutCreateWindow("Test");

	glutDisplayFunc(displayCallback);
	glutReshapeFunc(reshapeCallback);
	// Call a function after a specified ammount of time
	glutTimerFunc(1000, timer, 0);

	glClearColor(0.0f, 0.0f, 0.0f, 1.0f);

	glutMainLoop();
}