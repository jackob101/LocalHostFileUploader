import { Route, Switch } from "react-router";
import "./App.css";
import MainPage from "./pages/MainPage/MainPage";
import { Toaster } from "react-hot-toast";
import Note from "./pages/NotePage/Note";

function App() {
    return (
        <div
            // className="App"
            style={{ minHeight: "100vh" }}
            className="row"
        >
            <Switch>
                <Route path="/" exact>
                    <MainPage />
                </Route>
                <Route path="/note">
                    <Note />
                </Route>
            </Switch>
            <Toaster />
        </div>
    );
}

export default App;
