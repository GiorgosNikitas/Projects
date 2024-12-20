import { Route, Routes } from "react-router-dom";
import General from "./General";
import Header from "./Header";
import Courses from "./Courses";
import Students from "./Students";
import MyNavbar from "./MyNavbar";

function App() {
    return (
        <div className="App">
            {/*<Header></Header>*/}
            <MyNavbar></MyNavbar>
            {/*<Routes>*/}
            {/*    <Route path="/" element={<General />} />*/}
            {/*    <Route path="/courses" element={<Courses />} />*/}
            {/*    <Route path="/students" element={<Students />} />*/}
            {/*    <Route path="/assignment" element={<General />} />*/}
            {/*    <Route path="/statistics" element={<General />} />*/}
            {/*</Routes>*/}
        </div>
    );
}

export default App;