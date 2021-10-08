import { Route, Switch } from "react-router";
import "./App.css";
import Navbar from "./components/Navbar/Navbar";
import MainPage from "./pages/MainPage/MainPage";

function App() {
  return (
    <div className="App">
      <Navbar />
      <Switch>
        <Route path="/" exact>
          <MainPage />
        </Route>
      </Switch>
    </div>
  );
}

export default App;
