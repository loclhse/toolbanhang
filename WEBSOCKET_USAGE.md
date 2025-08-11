# WebSocket Integration Guide

## Overview

This application now includes real-time communication capabilities using WebSocket technology. This allows for instant updates to be pushed from the server to connected clients without requiring clients to poll for changes.

## Key Features

- Real-time order status updates
- Real-time payment status updates
- General messaging capabilities
- Automatic reconnection handling
- SockJS fallback for browsers without WebSocket support

## Implementation Details

### Server-Side Components

1. **WebSocketConfig**: Configures the WebSocket endpoints and message broker
   - Located at: `src/main/java/com/josh/foodorder/config/WebSocketConfig.java`
   - Enables a simple in-memory message broker
   - Configures the `/ws` endpoint with SockJS fallback support

2. **WebSocketController**: Handles incoming WebSocket messages
   - Located at: `src/main/java/com/josh/foodorder/controller/WebSocketController.java`
   - Provides message mapping for `/app/message` and `/app/order-update`
   - Broadcasts messages to appropriate topics

3. **WebSocketService**: Service for sending WebSocket messages programmatically
   - Located at: `src/main/java/com/josh/foodorder/service/WebSocketService.java`
   - Provides methods to send messages to specific topics
   - Used by other services to broadcast updates

4. **WebSocketMessage**: DTO for WebSocket messages
   - Located at: `src/main/java/com/josh/foodorder/dto/WebSocketMessage.java`
   - Generic class that can hold any payload type
   - Includes message type, payload, and timestamp

### Client-Side Components

1. **websocket-client.js**: JavaScript client for WebSocket communication
   - Located at: `src/main/resources/static/js/websocket-client.js`
   - Provides methods for connecting, disconnecting, and sending messages
   - Handles subscription to topics and message processing

2. **websocket-demo.html**: Demo page for testing WebSocket functionality
   - Located at: `src/main/resources/static/websocket-demo.html`
   - Provides a UI for connecting to WebSocket and sending/receiving messages
   - Useful for testing and understanding the WebSocket implementation

## Message Topics

| Topic | Description | Used For |
|-------|-------------|----------|
| `/topic/messages` | General messages | System notifications, general updates |
| `/topic/orders` | Order updates | Order status changes, new orders |
| `/topic/payments` | Payment updates | Payment status changes, new payments |

## Integration with Existing Services

The WebSocket functionality has been integrated with the following services:

1. **OrderService**: Broadcasts order status changes
   - When an order is marked as "DONE", a message is sent to `/topic/orders`

2. **PaymentService**: Broadcasts payment status changes
   - When a payment is initiated or confirmed, a message is sent to `/topic/payments`

## How to Use in Frontend Applications

### 1. Include Required Libraries

```html
<script src="https://cdn.jsdelivr.net/npm/sockjs-client@1.5.1/dist/sockjs.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/stomp-websocket@2.3.4/lib/stomp.min.js"></script>
<script src="/js/websocket-client.js"></script>
```

### 2. Connect to WebSocket Server

```javascript
// Connect to WebSocket server
websocketClient.connect();

// Set up connection event handlers
window.onConnected = function() {
    console.log('Connected to WebSocket server');
};

window.onDisconnected = function() {
    console.log('Disconnected from WebSocket server');
};

window.onConnectionError = function(error) {
    console.error('Connection error:', error);
};
```

### 3. Set Up Message Handlers

```javascript
// Handle order updates
websocketClient.setOrderUpdateHandler(function(message) {
    console.log('Order update received:', message);
    // Update UI based on order status
    updateOrderUI(message.payload);
});

// Handle payment updates
websocketClient.setPaymentUpdateHandler(function(message) {
    console.log('Payment update received:', message);
    // Update UI based on payment status
    updatePaymentUI(message.payload);
});

// Handle general messages
websocketClient.setMessageHandler(function(message) {
    console.log('Message received:', message);
    // Show notification or update UI
    showNotification(message.payload);
});
```

### 4. Send Messages (if needed)

```javascript
// Send a message to a specific destination
websocketClient.sendMessage('/app/message', {
    type: 'MESSAGE',
    payload: {
        content: 'Hello, WebSocket!',
        importance: 'high'
    }
});

// Send an order update
websocketClient.sendMessage('/app/order-update', {
    type: 'ORDER_UPDATE',
    payload: {
        orderId: '123e4567-e89b-12d3-a456-426614174000',
        status: 'PENDING'
    }
});
```

## Testing WebSocket Functionality

You can test the WebSocket functionality using the provided demo page:

1. Start the application
2. Open `http://localhost:8080/websocket-demo.html` in your browser
3. Click "Connect" to establish a WebSocket connection
4. Use the form to send test messages
5. Observe the message log for incoming messages

## Troubleshooting

### Connection Issues

- Ensure the application is running
- Check browser console for errors
- Verify that the WebSocket endpoint is correctly configured
- Check for network issues or firewalls blocking WebSocket connections

### Message Not Received

- Verify that you're subscribed to the correct topic
- Check that the message format is correct
- Ensure that the WebSocket connection is established before sending messages

## Security Considerations

- The current implementation does not include authentication for WebSocket connections
- In a production environment, consider adding authentication and authorization for WebSocket endpoints
- Implement message validation to prevent malicious payloads