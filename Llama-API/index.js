const express = require("express");

const { GradientLLM } = require("@langchain/community/llms/gradient_ai");
const { accessToken, workspaceId } = require("./config.json");

const sleep = (milliseconds) => new Promise((resolve) => setTimeout(resolve, milliseconds));

const app = express();
const PORT = process.env.PORT || 3000;
var bodyParser = require("body-parser");

const API_VERSION = "v0";

app.use(bodyParser.json());
app.use(bodyParser.urlencoded({ extended: false }));

let chat_history = {};

class Conversation {
    username = null;
    history = [];
    constructor(username) {
        this.username = username;
    }

    AddUserMessage(text) {
        let message = {
            isFrom: this.username,
            text,
        };
        this.history.push(message);
    }

    AddAiResponse(text) {
        let response = {
            isFrom: "AI",
            text,
        };
        this.history.push(response);
    }
}

app.post(`/verify-username`, async (req, res) => {
    const { username } = req.body;
    if (!username) {
        return res.status(400);
    }
    console.log(username);

    if (username in chat_history) {
        return res.status(400).json({ message: "This username already exists. Please choosse another" });
    }

    let filteredUsername = username.trim();
    if (filteredUsername.indexOf(" ") != -1) {
        return res.status(400).json({ message: "Invalid username. Spaces are not allowed." });
    }
    const convo = new Conversation(filteredUsername);
    chat_history[filteredUsername] = convo;

    console.log(`new conversation started wtih ${filteredUsername}`);
    return res.status(200).json({ message: filteredUsername });
    // return res.status(200).json({message: "Success!"});
});

app.post("/chat", async (req, res) => {
    const model = new GradientLLM({
        gradientAccessKey: accessToken,
        workspaceId,
        modelSlug: "llama3-70b-chat",
        inferenceParameters: {
            maxGeneratedTokenCount: 500,
            temperature: 0.9,
        },
    });

    const { username, message } = req.body;
    if (!username || !message) {
        console.log(username, message);
        return res.status(400).json({ message: "Invalid username/message sent in post request" });
    }
    
    let filteredUsername = username.trim();

    if (!(filteredUsername in chat_history)) {
        return res.status(401).json({ message: "Username not registered" });
    }

    const conversation = chat_history[filteredUsername];

    conversation.AddUserMessage(message.trim());

    const query = `[INST]
    You are a friendly chat bot. You are talking to a user called '${username}'.
    You are referred to as 'AI' in the chat history. Your random seed is ${Math.random()}
    Here is the current conversation in sequential order:
    ${JSON.stringify(conversation.history)}

    Your response should only include your reply to the user's most recent message, with out any quote marks at the start or end.
    If you want to reply with any new linCes, use \n.
    [/INST]`;

    const result = await model.invoke(query);
    console.log(result);

    conversation.AddAiResponse(result);

    return res.status(200).json({ message: result });
});

app.listen(PORT, () => {
    console.log(`Server is running on port ${PORT}`);
});
