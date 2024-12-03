import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Processor from './processor';
import ProcessorDetail from './processor-detail';
import ProcessorUpdate from './processor-update';
import ProcessorDeleteDialog from './processor-delete-dialog';

const ProcessorRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Processor />} />
    <Route path="new" element={<ProcessorUpdate />} />
    <Route path=":id">
      <Route index element={<ProcessorDetail />} />
      <Route path="edit" element={<ProcessorUpdate />} />
      <Route path="delete" element={<ProcessorDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default ProcessorRoutes;
