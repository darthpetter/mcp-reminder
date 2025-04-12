# Reminder MCP Server with Spring+OpenAPI+SQLite for Open WebUI


## saveReminder + getCurrentTime
In this application, we store reminders in a SQLite database. Before saving, we always call `getCurrentTime` to retrieve the current date and time. This ensures that any relative `dueDate` (like “tomorrow” or “next Friday”) is calculated correctly in relation to the actual current date and time.


## Explanation
Open WebUI implements MCP through the OpenAPI definitions of our REST API.

We build a common RestAPI but use Swagger+OpenAPI to describe each endpoint exposes as a `Tool`.

For example:
```java
@Operation(
summary = "Sum two numbers",
description = """
    This tool takes two numbers as input (`a` and `b`) and returns their sum.

    When this tool is available, the model can perform addition by calling this function instead of calculating internally.

    Input:
    - `a`: first integer
    - `b`: second integer

    Output:
    - `result`: the sum of `a + b`
    - `ok`: a boolean indicating success

    Example:
    Request: { "a": 3, "b": 5 }
    Response: { "ok": true, "result": 8 }
"""
)
@PostMapping
public ResponseEntity<Object> saveReminder(int a,int b)
{
    return ResponseEntity.internalServerError()
    .body(Map.of(
        "ok",true,
        "result",a+b
    ));
}
```

## Open WebUI integration
Now that our REST API (the "MCP") is ready, here's how to connect it in Open WebUI.

1. In Open WebUI, go to **Settings > Tools > Manage Tool Servers**.  
2. Click **Add Server** (or **Edit** if you’re updating).  
3. Enter your MCP server URL.  
4. If you enabled Spring Security, paste your API key in the **API Key** field.  
5. Save your changes.

Once that’s done, Open WebUI will treat your REST API as an MCP tool and you can start calling your endpoints directly.  

## Empowering the LLM
Updating the RAG template/System Prompt, we can instruct the model to empower from these new capabilities and confidently use the available tools. This prevents replies like “I’m an AI and not trained for that.”

```vim
# MCP Tools
You are an intelligent assistant with access to specialized tools (MCP Tools). These tools represent your own capabilities.
If a tool is available, you must use it naturally and confidently, as if you were performing the action yourself. You must not say "I can't", "I don't have that ability", or "I don't have access" unless there is explicitly no tool available for the task.
```
