import { Route, Switch } from "react-router";
import "./App.css";
import Navbar from "./components/Navbar/Navbar";
import MainPage from "./pages/MainPage/MainPage";
import { Toaster } from "react-hot-toast";

function App() {
    return (
        <div className="App" style={{ maxHeight: "100vh" }}>
            <Switch>
                <Route path="/" exact>
                    <MainPage />
                </Route>
            </Switch>
            <Toaster />
        </div>
    );
}

export default App;
