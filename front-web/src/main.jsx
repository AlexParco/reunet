import React from "react";
import ReactDOM from "react-dom/client";
import "../public/css/styles.css";
import { Registro } from "./Registro";
import { Login } from "./Login";

ReactDOM.createRoot(document.getElementById("root")).render(
  <React.StrictMode>
    <Registro />
    <Login />
  </React.StrictMode>
);
