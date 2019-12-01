#include <iostream>
#include <GL/glew.h>
#include <GL/freeglut.h>

void displayCallback()
{
	glClear(GL_COLOR_BUFFER_BIT);
	glLoadIdentity();	// Reset the parameters of the model-view matrix 

	// Draw calls
	glPointSize(10);
	glBegin(GL_TRIANGLES);
	
	// Specify Vertices
	glVertex2f(0.0f, 5.0f);
	glVertex2f(-4.0f, -3.0f);
	glVertex2f(4.0f, -3.0f);

	glEnd();

	glFlush();
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

int main(int argc, char** argv)
{
	glutInit(&argc, argv);
	glutInitDisplayMode(GLUT_RGB);

	glutInitWindowPosition(200, 100);
	glutInitWindowSize(500, 500);

	glutCreateWindow("Test");

	glutDisplayFunc(displayCallback);
	glutReshapeFunc(reshapeCallback);
	
	glClearColor(0.0f, 0.0f, 0.0f, 1.0f);

	glutMainLoop();

	return 0;
}
