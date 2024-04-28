import React from 'react';
import { render, screen, fireEvent, waitFor } from '@testing-library/react';
import axios from 'axios';
import ChatContent from '../components/chat/ChatContent';

jest.mock('axios');

describe('<ChatContent />', () => {
    const mockProps = {
        selectedUser: 'John',
        selectedUserImageContent: 'someBase64',
        userIdSelected: 'someUserId',
    };

    const mockChatResponse = [
        {
            content: 'Hello',
            messageType: 'SENT',
        },
        {
            content: 'Hi',
            messageType: 'RECEIVED',
        },
    ];

    beforeEach(() => {
        axios.get.mockResolvedValueOnce({ data: { response: mockChatResponse } });

        // Mock scrollIntoView
        Element.prototype.scrollIntoView = jest.fn();
    });

    afterEach(() => {
        // Clear mocks
        jest.clearAllMocks();
    });

    it('renders chat items', async () => {
        render(<ChatContent {...mockProps} />);

        await waitFor(() => {
            expect(screen.getByText('Hello')).toBeInTheDocument();
            expect(screen.getByText('Hi')).toBeInTheDocument();
        });
    });

    it('sends a message', async () => {
        axios.post.mockResolvedValueOnce({ data: { response: { content: 'New Message' } } });

        render(<ChatContent {...mockProps} />);

        const input = screen.getByPlaceholderText('Type a message here');
        const sendButton = document.getElementById('sendMsgBtn');


        fireEvent.change(input, { target: { value: 'New Message' } });
        fireEvent.click(sendButton);

        await waitFor(() => {
            expect(input.value).toBe('New Message');
        });
    });
});
