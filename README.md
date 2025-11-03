Installation:
  - Go into directory /game
  - Build the container using docker : docker build -t {ContainerName}
  - Allow X11 connexions for display : xhost +local:docker
  - Run the container : docker run -it --rm -e DISPLAY=$DISPLAY -v /tmp/.X11-unix:/tmp/.X11-unix:rw --network host javafx-game
  - GAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAMING !
