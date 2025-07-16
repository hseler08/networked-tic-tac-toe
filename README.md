# networked-tic-tac-toe
This repository contains a distributed implementation of the classic Tic-Tac-Toe game using a hybrid network architecture that combines Java RMI and TCP/IP sockets. The system allows multiple players to join, be matched into game sessions, and play against each other in a fully managed server environment.

Project Overview
Developed in a client-server architecture
Server handles:
  Game logic
  Player matchmaking
  Game session management
  In-game statistics tracking
Clients connect to the server via RMI and sockets
Server supports multiple concurrent games

Core Features
Hybrid Networking
  RMI (Remote Method Invocation) is used for:
    Player matchmaking
    Session management
    Move exchange and validation

  TCP/IP Sockets are used optionally for:
    Real-time updates or extended messaging
    Additional data transport if needed

Game Engine (on Server)
  Full game logic resides server-side
  Centralized validation of moves
  Turn management and win condition checks
  Thread-safe session isolation
