# Debugging the Java Backend

## Running with Debug Mode

```bash
# Run the application with debug mode enabled
./mvnw spring-boot:run -Dspring-boot.run.jvmArguments="-Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=5005"
```

## Testing the API with curl

```bash
# Test the checkout endpoint with sample data
curl -X POST http://localhost:8080/api/checkout \
  -H "Content-Type: application/json" \
  -d @src/main/resources/sample-checkout-request.json \
  -v
```

## Viewing Logs

```bash
# View application logs
tail -f logs/kata-app.log
```

## Remote Debugging with IDE

1. Create a Remote Debug Configuration in your IDE
   - Set host to: localhost
   - Set port to: 5005
   - Set transport to: Socket

2. Start the application with debug mode as shown above

3. Connect your IDE debugger to the running application

4. Set breakpoints in your code and debug

## Common Debugging Points

- `CheckoutController.checkout()` - Entry point for API requests
- `CheckoutService.calculateTotal()` - Business logic for calculating totals
- Request/Response DTOs - Check for proper serialization/deserialization

## Troubleshooting Common Issues

1. **404 Not Found**
   - Check the URL path and RequestMapping annotations
   - Verify the application is running on the expected port

2. **400 Bad Request**
   - Check the request payload against the DTO structure
   - Look for validation errors in the logs

3. **500 Internal Server Error**
   - Check the logs for exceptions
   - Set breakpoints in the service layer to trace execution

4. **Connection Refused**
   - Verify the application is running
   - Check the port configuration