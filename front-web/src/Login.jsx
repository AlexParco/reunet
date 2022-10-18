export const Login = () => {
  return (
    <>
      <div id="content">
        <div id="imagen">
          <img src="../public/img/logo.jpg" alt="logo" />
        </div>
        <div id="registro">
          <h1>Sing In</h1>
          <p>
            Don't have accoun't? <a href="">Sign up</a>
          </p>
          <div id="user">
            <img src="../public/img/user.png" alt="" />
          </div>
          <form id="formulario">
            <input type="text" />
            <input type="text" />
            <input type="submit" value="Login" />
          </form>
        </div>
      </div>
    </>
  );
};
