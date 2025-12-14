import React, { useState, useEffect } from 'react';
import { Play, Pause, RotateCcw, SkipForward } from 'lucide-react';

const BFSVisualization = () => {
  // Graph structure: adjacency list
  const graph = {
    0: [1, 2],
    1: [0, 3, 4],
    2: [0, 5, 6],
    3: [1],
    4: [1, 7],
    5: [2],
    6: [2, 8],
    7: [4],
    8: [6]
  };

  // Node positions for visualization
  const nodePositions = {
    0: { x: 250, y: 50 },
    1: { x: 150, y: 150 },
    2: { x: 350, y: 150 },
    3: { x: 50, y: 250 },
    4: { x: 200, y: 250 },
    5: { x: 300, y: 250 },
    6: { x: 450, y: 250 },
    7: { x: 200, y: 350 },
    8: { x: 450, y: 350 }
  };

  const [currentStep, setCurrentStep] = useState(0);
  const [isPlaying, setIsPlaying] = useState(false);
  const [visited, setVisited] = useState(new Set());
  const [queue, setQueue] = useState([0]);
  const [currentNode, setCurrentNode] = useState(null);
  const [visitOrder, setVisitOrder] = useState([0]);
  const [edges, setEdges] = useState([]);

  // Generate BFS steps
  const generateBFSSteps = () => {
    const steps = [];
    const vis = new Set();
    const q = [0];
    const order = [0];
    
    vis.add(0);
    steps.push({
      visited: new Set([0]),
      queue: [0],
      current: null,
      visitOrder: [0],
      message: "Start: Add node 0 to queue and mark as visited"
    });

    while (q.length > 0) {
      const current = q.shift();
      
      steps.push({
        visited: new Set(vis),
        queue: [...q],
        current: current,
        visitOrder: [...order],
        message: `Process node ${current} (dequeue)`
      });

      for (const neighbor of graph[current]) {
        if (!vis.has(neighbor)) {
          vis.add(neighbor);
          q.push(neighbor);
          order.push(neighbor);
          
          steps.push({
            visited: new Set(vis),
            queue: [...q],
            current: current,
            visitOrder: [...order],
            message: `Found unvisited neighbor ${neighbor}, add to queue`
          });
        }
      }
    }

    steps.push({
      visited: new Set(vis),
      queue: [],
      current: null,
      visitOrder: [...order],
      message: "Complete! Queue is empty, all reachable nodes visited"
    });

    return steps;
  };

  const [steps] = useState(generateBFSSteps());

  useEffect(() => {
    if (currentStep < steps.length) {
      const step = steps[currentStep];
      setVisited(step.visited);
      setQueue(step.queue);
      setCurrentNode(step.current);
      setVisitOrder(step.visitOrder);
    }
  }, [currentStep, steps]);

  useEffect(() => {
    let interval;
    if (isPlaying && currentStep < steps.length - 1) {
      interval = setInterval(() => {
        setCurrentStep(prev => prev + 1);
      }, 1500);
    } else if (currentStep >= steps.length - 1) {
      setIsPlaying(false);
    }
    return () => clearInterval(interval);
  }, [isPlaying, currentStep, steps.length]);

  const reset = () => {
    setCurrentStep(0);
    setIsPlaying(false);
  };

  const nextStep = () => {
    if (currentStep < steps.length - 1) {
      setCurrentStep(prev => prev + 1);
    }
  };

  const getNodeColor = (nodeId) => {
    if (currentNode === nodeId) return '#ef4444'; // red - currently processing
    if (visited.has(nodeId)) return '#10b981'; // green - visited
    return '#94a3b8'; // gray - unvisited
  };

  const getNodeLabel = (nodeId) => {
    if (currentNode === nodeId) return 'Processing';
    if (visited.has(nodeId)) return 'Visited';
    return 'Unvisited';
  };

  // Draw edges
  const renderEdges = () => {
    const edgeSet = new Set();
    const edgeElements = [];
    
    Object.keys(graph).forEach(node => {
      graph[node].forEach(neighbor => {
        const edgeKey = [parseInt(node), neighbor].sort().join('-');
        if (!edgeSet.has(edgeKey)) {
          edgeSet.add(edgeKey);
          const start = nodePositions[node];
          const end = nodePositions[neighbor];
          
          edgeElements.push(
            <line
              key={edgeKey}
              x1={start.x}
              y1={start.y}
              x2={end.x}
              y2={end.y}
              stroke="#cbd5e1"
              strokeWidth="2"
            />
          );
        }
      });
    });
    
    return edgeElements;
  };

  return (
    <div className="w-full max-w-4xl mx-auto p-6 bg-gray-50 rounded-lg">
      <h2 className="text-2xl font-bold mb-4 text-gray-800">BFS Traversal Visualization</h2>
      
      {/* Graph Display */}
      <div className="bg-white rounded-lg shadow-md p-4 mb-4">
        <svg width="500" height="400" className="mx-auto">
          {renderEdges()}
          {Object.keys(nodePositions).map(nodeId => {
            const pos = nodePositions[nodeId];
            const color = getNodeColor(parseInt(nodeId));
            
            return (
              <g key={nodeId}>
                <circle
                  cx={pos.x}
                  cy={pos.y}
                  r="25"
                  fill={color}
                  stroke="#1e293b"
                  strokeWidth="2"
                />
                <text
                  x={pos.x}
                  y={pos.y + 5}
                  textAnchor="middle"
                  fill="white"
                  fontSize="18"
                  fontWeight="bold"
                >
                  {nodeId}
                </text>
              </g>
            );
          })}
        </svg>
      </div>

      {/* Controls */}
      <div className="flex gap-2 mb-4 justify-center">
        <button
          onClick={() => setIsPlaying(!isPlaying)}
          className="px-4 py-2 bg-blue-500 text-white rounded hover:bg-blue-600 flex items-center gap-2"
        >
          {isPlaying ? <Pause size={20} /> : <Play size={20} />}
          {isPlaying ? 'Pause' : 'Play'}
        </button>
        <button
          onClick={nextStep}
          disabled={currentStep >= steps.length - 1}
          className="px-4 py-2 bg-green-500 text-white rounded hover:bg-green-600 disabled:bg-gray-300 flex items-center gap-2"
        >
          <SkipForward size={20} />
          Next
        </button>
        <button
          onClick={reset}
          className="px-4 py-2 bg-gray-500 text-white rounded hover:bg-gray-600 flex items-center gap-2"
        >
          <RotateCcw size={20} />
          Reset
        </button>
      </div>

      {/* Step Info */}
      <div className="bg-white rounded-lg shadow-md p-4 mb-4">
        <div className="mb-2">
          <span className="font-semibold">Step {currentStep + 1} of {steps.length}:</span>
          <span className="ml-2">{steps[currentStep]?.message}</span>
        </div>
        <div className="grid grid-cols-2 gap-4 mt-4">
          <div>
            <h3 className="font-semibold mb-2">Queue Contents:</h3>
            <div className="flex gap-2">
              {queue.length > 0 ? (
                queue.map((node, idx) => (
                  <div key={idx} className="px-3 py-1 bg-blue-100 border-2 border-blue-500 rounded">
                    {node}
                  </div>
                ))
              ) : (
                <span className="text-gray-500">Empty</span>
              )}
            </div>
          </div>
          <div>
            <h3 className="font-semibold mb-2">Visit Order:</h3>
            <div className="text-lg">
              {visitOrder.join(' → ')}
            </div>
          </div>
        </div>
      </div>

      {/* Legend */}
      <div className="bg-white rounded-lg shadow-md p-4">
        <h3 className="font-semibold mb-2">Legend:</h3>
        <div className="flex gap-6">
          <div className="flex items-center gap-2">
            <div className="w-6 h-6 rounded-full bg-gray-400"></div>
            <span>Unvisited</span>
          </div>
          <div className="flex items-center gap-2">
            <div className="w-6 h-6 rounded-full bg-red-500"></div>
            <span>Currently Processing</span>
          </div>
          <div className="flex items-center gap-2">
            <div className="w-6 h-6 rounded-full bg-green-500"></div>
            <span>Visited</span>
          </div>
        </div>
      </div>
    </div>
  );
};

export default BFSVisualization;
