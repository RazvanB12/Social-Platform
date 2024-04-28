import React from 'react';
import { render, fireEvent, waitFor, screen } from '@testing-library/react';
import { MemoryRouter } from 'react-router-dom';
import axios from 'axios';
import SignUp from '../components/registration/register';

jest.mock('axios');

describe('SignUp component', () => {
    test('renders SignUp component', () => {
        render(
            <MemoryRouter>
                <SignUp />
            </MemoryRouter>
        );
        expect(screen.getByText('Sign Up')).toBeInTheDocument();
    });

    test('allows user to fill the form and sign up', async () => {
        axios.post.mockResolvedValueOnce({ data: {} });

        render(
            <MemoryRouter>
                <SignUp />
            </MemoryRouter>
        );

        // Fill the form fields
        fireEvent.change(screen.getByPlaceholderText('Enter first name'), {
            target: { value: 'John' },
        });
        fireEvent.change(screen.getByPlaceholderText('Enter last name'), {
            target: { value: 'Doe' },
        });
        fireEvent.change(screen.getByPlaceholderText('Enter email'), {
            target: { value: 'john.doe@example.com' },
        });
        fireEvent.change(screen.getByPlaceholderText('Enter password'), {
            target: { value: 'Password123#' },
        });
        fireEvent.change(screen.getByPlaceholderText('Confirm your password'), {
            target: { value: 'Password123#' },
        });
        fireEvent.click(screen.getByLabelText('yes'));

        fireEvent.click(screen.getByText('Sign up'));


        await waitFor(() => {
            expect(axios.post).toHaveBeenCalledWith(
                'http://localhost:8080/register',
                {
                    firstName: 'John',
                    lastName: 'Doe',
                    email: 'john.doe@example.com',
                    password: 'Password123#',
                    publicContent: true,
                }
            );
        });


        expect(screen.getByText('Sign Up')).toBeInTheDocument();
    });

    test('displays error messages for incomplete form', () => {
        render(
            <MemoryRouter>
                <SignUp />
            </MemoryRouter>
        );

        fireEvent.click(screen.getByText('Sign up'));

        expect(screen.getByText('Not all required fields are complete!')).toBeInTheDocument();
    });

    test('displays error messages for invalid email format', () => {
        render(
            <MemoryRouter>
                <SignUp />
            </MemoryRouter>
        );

        fireEvent.change(screen.getByPlaceholderText('Enter email'), {
            target: { value: 'invalidemail' },
        });

        expect(screen.getByText('Invalid email format!')).toBeInTheDocument();
    });

    test('displays error message for weak password', () => {
        render(
            <MemoryRouter>
                <SignUp />
            </MemoryRouter>
        );

        fireEvent.change(screen.getByPlaceholderText('Enter password'), {
            target: { value: 'weakpassword' },
        });

        expect(
            screen.getByText(
                'Password must contain at least one letter, one number, one special character, and have a minimum length of 8'
            )
        ).toBeInTheDocument();
    });

    test('displays error message for mismatched passwords', () => {
        render(
            <MemoryRouter>
                <SignUp />
            </MemoryRouter>
        );

        fireEvent.change(screen.getByPlaceholderText('Enter password'), {
            target: { value: 'Password123#' },
        });
        fireEvent.change(screen.getByPlaceholderText('Confirm your password'), {
            target: { value: 'MismatchedPassword' },
        });

        expect(screen.getByText('Passwords do not match!')).toBeInTheDocument();
    });
});
