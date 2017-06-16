#!BPY

import struct


"""
Name: '3DBT Exporter'
Blender: 277
Group: 'Export'
Tooltip: '3DBT Exporter'
"""

bl_info = {
    "name": "DBT Exporter",
    "category": "Export",
}

import bpy


def writechar(file, c):
  #file.write("%c" % ord(c))
  file.write( c )

def writefloat(file, f):
  file.write(struct.pack('>f', f))

def writeint(file, i):
  file.write(struct.pack('>i', i))

def writeshort(file, s):
  file.write(struct.pack('>h', s))

def writelong(file, l):
  file.write(struct.pack('>l', l))


class EmptyUV:
  uv = (0.0, 0.0)
  def __getitem__(self, index): return self
  
class EmptyColor:
  color = (1.0, 1.0, 1.0)
  def __getitem__(self, index): return self
  
class Submesh:
  def __init__(self, model_name):
    self.mesh = bpy.data.meshes[model_name]
    self.data = bpy.data.objects[model_name]
	
  def export(self, out):
    uv_act = self.mesh.uv_layers.active
    uv_layer = uv_act.data if (uv_act is not None) else EmptyUV()
    #color_data = self.mesh.vertex_colors[0].data
    color_data = self.mesh.vertex_colors[0].data if (self.mesh.vertex_colors[0].data is not None and len(self.mesh.vertex_colors[0].data)>0) else EmptyColor()
    verts = self.mesh.vertices

    #name and texture
    writechar(out, bytes((len(self.mesh.name),)))
    for i in self.mesh.name:
       writechar(out, bytes((ord(i),)))
	#texture filename???
    writechar(out, bytes((len(self.mesh.name),)))
    for i in self.mesh.name:
       writechar(out, bytes((ord(i),)))

    padding = 4 - (len(self.mesh.name)+len(self.mesh.name)+2+1) % 4
    writechar(out, bytes((padding,)))
    for i in range(padding):
       writechar(out, b'\x00')
	
	
    writeint(out,len(self.mesh.polygons)*3)  #vertex
    writeint(out,len(self.mesh.polygons)*3)  #normals
    writeint(out,len(self.mesh.polygons)*3)  #uv
    writeint(out,len(self.mesh.polygons)*3)  #color
	
    loop_vert = {l.index: l.vertex_index for l in self.mesh.loops}
    for face in self.mesh.polygons:
        for li in face.loop_indices:
            #print("Normal:", *verts[loop_vert[li]].normal)
            #print("Position:", *verts[loop_vert[li]].co)
            #print("UV:", *uv_layer[li].uv)
            #print("Color:", *color_data[li].color)
            writefloat(out, float(verts[loop_vert[li]].co.x))
            writefloat(out, float(verts[loop_vert[li]].co.y))
            writefloat(out, float(verts[loop_vert[li]].co.z))
			
    for face in self.mesh.polygons:
        for li in face.loop_indices:			
			
            writefloat(out, float(verts[loop_vert[li]].normal.x))
            writefloat(out, float(verts[loop_vert[li]].normal.y))
            writefloat(out, float(verts[loop_vert[li]].normal.z))
			
    for face in self.mesh.polygons:
        for li in face.loop_indices:			
			
            writefloat(out, float(uv_layer[li].uv[0]))
            writefloat(out, float(uv_layer[li].uv[1]))
            #writefloat(out, float(0.0))			
            #writefloat(out, float(0.0))						
			
    for face in self.mesh.polygons:
        for li in face.loop_indices:			
			
            writechar(out, bytes((int(color_data[li].color.r*255),)))
            writechar(out, bytes((int(color_data[li].color.g*255),)))
            writechar(out, bytes((int(color_data[li].color.b*255),)))
            #writechar(out, bytes((255,)))
            #writechar(out, bytes((255,)))
            #writechar(out, bytes((255,)))
			

    for face in self.mesh.polygons:
        for li in face.loop_indices:		
            writeshort(out, li)		
			

			
class Mesh:
  def __init__(self):
     pass
	 
  def export(self, filename):	 
    self.out = open(filename,"wb")
	#Header
    writechar(self.out, b'3') #Magic
    writechar(self.out, b'D')
    writechar(self.out, b'B')
    writechar(self.out, b'T')
    writechar(self.out, b'\x00') #version major, minor
    writechar(self.out, b'\x01')
	
    writeshort(self.out, len(bpy.data.meshes))   #number of meshes
    for m in bpy.data.meshes:
        submesh = Submesh(m.name)
        submesh.export(self.out)
	  
    self.out.close()
	
def write_obj(filepath):
    m = Mesh()
    m.export(filepath)
	  

if __name__ == "__main__":
    write_obj("kk.3dbt")	
    print("Model exported.")	
#Blender.Window.FileSelector(write_obj, "Export")
